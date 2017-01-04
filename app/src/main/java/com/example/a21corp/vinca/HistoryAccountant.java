package com.example.a21corp.vinca;

/**
 * Created by Thomas on 04-01-2017.
 */

public class HistoryAccountant {

    private static final long serialVersionUID = 12345;

    private static HistoryAccountant instance;

    private HistoryAccountant() {}

    public static HistoryAccountant getInstance() {
        if (instance == null) {
            instance = new HistoryAccountant();
        }
        return instance;
    }
    public static void setHistoryAccountant(HistoryAccountant historyAccountant){
        instance = historyAccountant;
    }
}
