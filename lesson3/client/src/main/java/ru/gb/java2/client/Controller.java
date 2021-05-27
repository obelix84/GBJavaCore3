package ru.gb.java2.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Controller {
    @FXML
    TextField msgField, usernameField, passwordField;

    @FXML
    TextArea msgArea;

    @FXML
    HBox loginPanel, msgPanel;

    @FXML
    ListView<String> clientList;


    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String username;
    private String login;
    private Thread dataThread;
    private boolean listening = true;
    private HistoryWriter historyWriter;


    public void sendMsg(ActionEvent actionEvent) {
        String msg = msgField.getText() + '\n';

        if(msg.startsWith("/exit")) {
            listening = false;
        }

        try {
            out.writeUTF(msg);
            msgField.clear();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Невозможно отправить сообщение", ButtonType.OK);
            alert.showAndWait();
        }

    }

    public void login(ActionEvent actionEvent) {
        if(socket == null || socket.isClosed()) {
            connect();
        }

        if(usernameField.getText().isEmpty() && passwordField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Имя пользователя и пароль не может быть пустым",
                    ButtonType.OK);
            alert.showAndWait();
            return;
        }

        this.login = usernameField.getText();
        try {
            out.writeUTF("/login " + usernameField.getText() + " " + passwordField.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username) {
        //очищаем поле сообщений
        Platform.runLater(() -> {
            msgArea.clear();
            clientList.getItems().clear();
        });

        this.username = username;
        if(username != null) {
            loginPanel.setVisible(false);
            loginPanel.setManaged(false);
            msgPanel.setVisible(true);
            msgPanel.setManaged(true);
        } else {
            loginPanel.setVisible(true);
            loginPanel.setManaged(true);
            msgPanel.setVisible(false);
            msgPanel.setManaged(false);

        }
    }

    public void connect() {
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            this.dataThread = new Thread(() -> {
                try {
                    //цикл авторизации
                    while (true) {
                        String msg = in.readUTF();

                        //login_ok Alex
                        if(msg.startsWith("/login_ok ")) {
                            String username = msg.split("\\s")[1];
                            setUsername(username);
                            listening = true;
                            //если пользователь залогинился создаем робъект для записи истории
                            this.historyWriter = new HistoryWriter(this.login);
                            break;
                        }

                        if(msg.startsWith("/login_failed ")) {
                            String cause = msg.split("\\s", 2)[1];
                            msgArea.appendText(cause + '\n');
                        }

                    }

                    //цикл общения
                    //добавим ВЫХОД
                    //вот тут  надо убрать работу цикла по /exit
                    //Еще возникает EOFexception в потоке чтения сообщений на клиенте, когда на сервер
                    // отправляешь /exit. Я его просто обработал и оставил без ошибок.
                    //Как "убить поток", который читает сообщения? Не понимаю.
                    // Вызов interrupt не помогает, изменить условие в цикле не помогает, он все равно ждет
                    // единичного ввода из потока.
                    // Можно отправить комманду от сервера на отключение... ну т.е. схема выхода
                    // такая клиент посылает /exit на сервер, а от сервера в свою очередь
                    // приходит подтверждение и тогда клиент "выключается".
                    // Но как-то такая зависимость от севрера не есть хорошо, как мне кажеться....
                    while (listening) {
                        String msg = in.readUTF();

                        if(msg.startsWith("/")) {
                            executeCommand(msg);
                            continue;
                        }

                        msgArea.appendText(msg);
                        //пишем сообщение в файл
                        if(!historyWriter.writeHistory(msg)) {
                            System.out.println("Не могу записать историю!\n");
                        }
                    }
                } catch (IOException e) {
                    //тут кидает EOFexception, если на сервер отправили комманду /exit, надо его обработать
                    if (listening) {
                        e.printStackTrace();
                    }
                } finally {
                    disconnect();
                    historyWriter.close();
                }
            });
            dataThread.start();

        } catch (IOException e) {
            throw new RuntimeException("Unable to connect to server [localhost: 8189]");
        }
    }

    private void executeCommand(String cmd) {
        if(cmd.startsWith("/clients_list ")) {
            String[] tokens = cmd.split("\\s");

            Platform.runLater(() -> {
                clientList.getItems().clear();
                for (int i = 1; i < tokens.length; i++) {
                    clientList.getItems().add(tokens[i]);
                }
            });
            return;
        }
    }

    public void disconnect() {
        setUsername(null);
        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
