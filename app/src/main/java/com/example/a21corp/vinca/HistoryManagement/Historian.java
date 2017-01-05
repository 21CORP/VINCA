package com.example.a21corp.vinca.HistoryManagement;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Thomas on 04-01-2017.
 */

public class Historian { //https://en.wikipedia.org/wiki/Command_pattern#Java
    private static final long serialVersionUID = 12345;
    private static Historian instance;

    private Stack<Command> historyStack = new Stack<Command>();
    private Stack<Command> redoStack= new Stack<Command>();

    private Historian() {}

    public static Historian getInstance() {
        if (instance == null) {
            instance = new Historian();
        }
        return instance;
    }

    public void storeAndExecute(Command cmd){
        historyStack.push(cmd);
        cmd.execute();
        Log.d("History", cmd.getClass().getSimpleName() + " added to history");
        redoStack.clear();
    }

    public void undo(){
        redoStack.push(historyStack.peek());
        historyStack.pop();
    }

    public void redo(){
        historyStack.push(redoStack.peek());
        redoStack.pop();
    }
}
