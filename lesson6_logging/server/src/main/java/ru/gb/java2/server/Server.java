package ru.gb.java2.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Server {
    private int port;
    private List<ClientHandler> clients;
    static Logger logger;

    private AuthService authService;

    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList<>();

        //Создаем логгер
        logger = Logger.getLogger(Server.class.getName());
        LogManager manager = LogManager.getLogManager();
        try {
            manager.readConfiguration(new FileInputStream("server/src/main/resources/logging.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Открываем доступ к БД
        //Прокидываем логгер в класс с БД
        if (!SQLHelper.connect()) {
            logger.severe("Не удалось подключиться к базе данных!");
            throw new RuntimeException("Не могу подключиться к БД");
        }
        //создаем сервис для доступа к данным пользователей
        authService = new AuthServiceDB();

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Сервер запущен на порту " + port + "...");
            while (true) {
                logger.info("Ждём нового клиента...");
                Socket socket = serverSocket.accept();
                logger.info("Клиент подключился");
                new ClientHandler(socket, this);
            }
        } catch (IOException e) {
            logger.severe("Не удалось открыть соединение!");
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
        logger.info("Клиент " + clientHandler.getUsername() + " подлючился");
        broadcastMessage("Клиент " + clientHandler.getUsername() + " подлючился \n");
        broadcastClientList();
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        logger.info("Клиент " + clientHandler.getUsername() + " отключился");
        broadcastMessage("Клиент " + clientHandler.getUsername() + " отключился \n");
        broadcastClientList();
    }

    public synchronized void broadcastMessage(String message) {
        logger.info("Отправка сообщения всем клиентам");
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
        logger.info("Отправка личного сообщение для " + nick);
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(nick)) {
                sender.sendMessage("Личное сообщение для " + nick + ": " + message);
                client.sendMessage("Личное сообщение от " + sender.getUsername() + ": " + message);
                return;
            }
        }
        logger.info("Клиента с ником " + nick + " нет на севрере");
        sender.sendMessage("Такого пользователя нет!\n");
    }

    public synchronized void  broadcastClientList() {
        logger.info("Отправка списка клиннтов все участникам");
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
