package ru.gb.java2.client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryReader {
    private String nick;
    private String filename;
    private RandomAccessFile reader;

    public HistoryReader(String nick) {
        this.nick = nick;
        this.filename = "history/history_"+ nick + ".txt";
    }

    public List<String> getLastHistory() {
        File f = new File(this.filename);
        if(f.exists() && f.isFile()) {
            try {
                this.reader = new RandomAccessFile(this.filename, "r");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                this.reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }
        return null;
    }
}
