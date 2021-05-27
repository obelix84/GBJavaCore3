package ru.gb.java2.server;

public class AuthServiceDB implements AuthService {


    @Override
    public String getNickname(String login, String password) {
        return SQLHelper.getNick(login, password);
    }

    @Override
    public boolean register(String login, String password, String nickname) {
        return SQLHelper.register(login, password, nickname);
    }

    @Override
    public boolean changeNickname(String oldNick, String newNick) {
        return SQLHelper.changeNick(oldNick, newNick);
    }
}
