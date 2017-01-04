package com.example.a21corp.vinca.HistoryManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 04-01-2017.
 */

public class HistoryAccountant { //https://en.wikipedia.org/wiki/Command_pattern#Java
    private static final long serialVersionUID = 12345;
    private static HistoryAccountant instance;

    private List<Command> historyList = new ArrayList<Command>();

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

    public void storeAndExecute(Command cmd){
        historyList.add(cmd);
        cmd.execute();
    }
}
