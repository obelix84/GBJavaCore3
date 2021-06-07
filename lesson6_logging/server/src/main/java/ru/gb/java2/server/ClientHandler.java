package ru.gb.java2.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.Format;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler {
    private Socket socket;
    private Server server;
    private DataInputStream in;
    private DataOutputStream out;
    private String username;
    private String login;
    private ExecutorService cachedService;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.server = server;

        this.cachedService = Executors.newCachedThreadPool();

        this.cachedService.execute(() -> {
            try {
                //цикл авторизации
                while (true) {
                    String msg = in.readUTF();
                    if(msg.startsWith("/")) {
                        if (executeCommand(msg)) break;
                    }
                }

                //цикл общения
                while (true) {
                    String msg = in.readUTF();
                    if(msg.startsWith("/")) {
                        if (executeCommand(msg)) break;
                        continue;
                    }
                    String message = String.format("<%s> %s",username, msg);
                    server.broadcastMessage(message);
                }
            } catch (IOException e) {
                Server.logger.severe("Ошибка чтения из сокета!");
                e.printStackTrace();
            } finally {
                disconnect();
                this.cachedService.shutdown();
            }
        });
    }
    //возвращает true когда необходимо сделать break из цикла, например при /exit
    //false означает продолжаем цикл сообщений, true прерываем
    private boolean executeCommand(String cmd) throws IOException {

        if(cmd.startsWith("/exit")) {
            // выходим из цикла
            Server.logger.info("Клиент отключился..");
            return true;
        }
        //авторизация /login login password
        if(cmd.startsWith("/login ")) {
            String login = cmd.split("\\s")[1];
            String pass = cmd.split("\\s")[2];
            //проверяем есть ли такой пользователь
            AuthService authService = server.getAuthService();
            String nick = authService.getNickname(login, pass);
            if(nick == null) {
                sendMessage("/login_failed Current user not found");
                return false;
            }

            this.username = nick;
            sendMessage("/login_ok " + this.username);
            server.subscribe(this);
            return true;
        }
        // /change new
        if(cmd.startsWith("/change")) {
            AuthService authService = server.getAuthService();
            String newNick = cmd.split("\\s")[1];
            if (authService.changeNickname(username, newNick)) {
                this.username = newNick;
                this.sendMessage("Ваш ник изменен на " + username + "\n");
                server.broadcastClientList();
            } else {
                this.sendMessage("Изменить ник не удалось! \n");
            }
            return false;
        }

        //регистрация нового пользователя !для отладки, потом форму прикручу
        // /register login password nick
        if(cmd.startsWith("/register")) {
            AuthService authService = server.getAuthService();
            String[] parts = cmd.split("\\s", 4);
            if (authService.register(parts[1], parts[2], parts[3])) {
                this.sendMessage("Регистрация нового пользователя прошла успешно! \n");
            } else {
                this.sendMessage("Зарегистрировать нового пользователя не удалолсь! \n");
            }
            return false;
        }
        if(cmd.startsWith("/who_am_i")) {
            this.sendMessage("Ваш логин "+ username + "\n");
            return false;
        }
        if(cmd.startsWith("/w ")) {
            //вот тут ощибки могут быть, т.к. строка не обязатнльно состоит из 3х частей
            String[] parts = cmd.split("\\s", 3);
            server.privateMessage(this, parts[2], parts[1]);
            return false;
        }
        return false;
    }

    private void disconnect() {
        server.unsubscribe(this);
        if(in != null) {
            try {
                in.close();
            } catch (IOException e) {
                Server.logger.severe("Ошибка закрытия соединения!");
                e.printStackTrace();
            }
        }
        if(out != null) {
            try {
                out.close();
            } catch (IOException e) {
                Server.logger.severe("Ошибка закрытия соединения!");
                e.printStackTrace();
            }
        }
        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                Server.logger.severe("Ошибка закрытия соединения!");
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            Server.logger.severe("Ошибка отправки сообщения клиенту!");
            disconnect();
        }
    }

    public String getUsername() {
        return username;
    }
}
