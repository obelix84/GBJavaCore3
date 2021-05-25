package ru.gb.java2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private int port;
    private List<ClientHandler> clients;

    private AuthService authService;

    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList<>();

        //Открываем джоступ к БД
        if (!SQLHelper.connect()) {
            throw new RuntimeException("Не могу подключиться к БД");
        };
        //создаем сервис для доступа к данным пользователей
        authService = new AuthServiceDB();

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port + "...");
            while (true) {
                System.out.println("Ждём нового клиента...");
                Socket socket = serverSocket.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(socket, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            SQLHelper.disconnect();
        }

    }

    public AuthService getAuthService() {
        return authService;
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastMessage("Клиент " + clientHandler.getUsername() + " подлючился \n");
        broadcastClientList();
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastMessage("Клиент " + clientHandler.getUsername() + " отключился \n");
        broadcastClientList();
    }

    public synchronized void broadcastMessage(String message) {
        for (ClientHandler clientHandler : clients) {
            clientHandler.sendMessage(message);
        }
    }

    public synchronized void commandMessage(ClientHandler sender, String message, String nick) {
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(nick)) {
                client.sendMessage(message);
                return;
            }
        }
        sender.sendMessage("Такого пользователя нет!");
    }

    public synchronized void privateMessage(ClientHandler sender, String message, String nick) {
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(nick)) {
                sender.sendMessage("Личное сообщение для " + nick + ": " + message);
                client.sendMessage("Личное сообщение от " + sender.getUsername() + ": " + message);
                return;
            }
        }
        sender.sendMessage("Такого пользователя нет!\n");
    }

    public synchronized void  broadcastClientList() {
        StringBuilder stringBuilder = new StringBuilder("/clients_list ");
        for (ClientHandler client : clients) {
            stringBuilder.append(client.getUsername()).append(" ");
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        String clientsList = stringBuilder.toString();
        for (ClientHandler clientHandler : clients) {
            clientHandler.sendMessage(clientsList);
        }
    }

    public boolean isNickBusy(String nickname) {
        for (ClientHandler clientHandler : clients) {
            if(clientHandler.getUsername().equals(nickname)) {
                return true;
            }
        }
        return false;
    }
}
