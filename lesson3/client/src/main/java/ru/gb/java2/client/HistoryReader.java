package ru.gb.java2.client;

import java.io.*;

public class HistoryReader {
    private String nick;
    private String filename;
    private RandomAccessFile reader;

    public HistoryReader(String nick) {
        this.nick = nick;
        this.filename = "history/history_"+ nick + ".txt";
        File f = new File(this.filename);
        if(f.exists() && f.isFile()) {
            System.out.println("файл существует");
        }
        try {
            this.reader = new RandomAccessFile(this.filename, "r");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
