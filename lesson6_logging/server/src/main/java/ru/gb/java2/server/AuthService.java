package ru.gb.java2.server;

public interface AuthService {
    String getNickname(String login, String password);
    boolean register(String login, String password, String nickname);
    boolean changeNickname(String oldNick, String newNick);
}
