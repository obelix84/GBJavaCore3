package ru.gb.java2.server;


import java.sql.*;


public class SQLHelper {
    private static Connection connection;
    private static PreparedStatement psGetNickname;
    private static PreparedStatement psRegister;
    private static PreparedStatement psChangeNick;

    public static boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            psGetNickname = connection.prepareStatement("SELECT id, login, password, nickname FROM users WHERE login = ? and password = ?;");
            psRegister = connection.prepareStatement("INSERT INTO users (login, password, nickname) VALUES (? ,? ,? );");
            psChangeNick = connection.prepareStatement("UPDATE users SET nickname = ? WHERE nickname = ?;");
            return true;
        } catch (Exception e) {
            Server.logger.severe("Ошибка соединения с базой данных или/и создания prepeared statements");
            e.printStackTrace();
            return false;
        }
    }
    public static void disconnect() {
        try {
            psGetNickname.close();
            psRegister.close();
            psChangeNick.close();
        } catch (SQLException e) {
            Server.logger.severe("Ошибка закрытия prepeared statements!");
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            Server.logger.severe("Ошибка соединения с БД!");
            e.printStackTrace();
        }
    }

    public static String getNick(String login, String password) {
        String nickname = null;
        try {
            psGetNickname.setString(1, login);
            psGetNickname.setString(2, password);
            ResultSet res = psGetNickname.executeQuery();
            if (res.next()) {
                nickname = res.getString("nickname");
            }
        } catch (SQLException sqlExc) {
            Server.logger.severe("Ошибка выполнения SQL запроса!");
            sqlExc.printStackTrace();
        }
        return nickname;
    }

    public static boolean changeNick(String oldNickname, String newNickname) {
        try {
            psChangeNick.setString(1, newNickname);
            psChangeNick.setString(2, oldNickname);
            psChangeNick.executeUpdate();
            return true;
        } catch (SQLException sqlExc) {
            Server.logger.severe("Ошибка выполнения SQL запроса!");
            sqlExc.printStackTrace();
            return false;
        }
    }

    public static boolean register(String login, String password, String nickname) {
        try {
            psRegister.setString(1, login);
            psRegister.setString(2, password);
            psRegister.setString(3, nickname);
            psRegister.executeUpdate();
            return true;
        } catch (SQLException sqlExc) {
            Server.logger.severe("Ошибка выполнения SQL запроса!");
            sqlExc.printStackTrace();
            return false;
        }
    }
}
