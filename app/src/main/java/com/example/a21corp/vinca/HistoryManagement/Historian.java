package com.example.a21corp.vinca.HistoryManagement;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 04-01-2017.
 */

public class Historian { //https://en.wikipedia.org/wiki/Command_pattern#Java
    private static final long serialVersionUID = 12345;
    private static Historian instance;

    private List<Command> historyList = new ArrayList<Command>();

    private Historian() {}

    public static Historian getInstance() {
        if (instance == null) {
            instance = new Historian();
        }
        return instance;
    }
    public static void setHistoryAccountant(Historian historian){
        instance = historian;
    }

    public void storeAndExecute(Command cmd){
        historyList.add(cmd);
        cmd.execute();
        Log.d("History", cmd.getClass().getSimpleName() + " added to history");
    }
}
