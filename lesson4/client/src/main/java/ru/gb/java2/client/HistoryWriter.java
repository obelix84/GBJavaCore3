package ru.gb.java2.client;

import java.io.*;

public class HistoryWriter {
    private String nick;
    private String filename;
    private BufferedWriter writer;
    //считает кол-во сообщений записанных в файл
    //нужно, чтобы сделать flush по записи определенного кол-ва  сообщений в буфер
    private int messageCounter = 0;
    //кол-во сообщений до записи
    private final int bufferedMessages = 10;


    public HistoryWriter(String nick) {
        this.nick = nick;
        this.filename = "history/history_" + nick + ".txt";
        try {
            this.writer = new BufferedWriter(new FileWriter(this.filename, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean writeHistory(String message) {
        messageCounter++;
        try {
            writer.write(message);
            if (messageCounter == bufferedMessages) {
                writer.flush();
                messageCounter = 0;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void close() {
        try {
            this.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
