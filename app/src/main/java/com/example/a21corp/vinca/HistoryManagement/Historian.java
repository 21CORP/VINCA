package com.example.a21corp.vinca.HistoryManagement;

import android.os.SystemClock;
import android.util.Log;
import android.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Thomas on 04-01-2017.
 */

public class Historian { //https://en.wikipedia.org/wiki/Command_pattern#Java
    private static final long serialVersionUID = 12345;
    private static Historian instance;
    public long timeSinceChange;
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
        timeSinceChange =SystemClock.uptimeMillis()/1000;
        redoStack.clear();
        Log.d("Autosave - timestamp", Long.toString(timeSinceChange));
    }

    public void undo(){
        if(!historyStack.isEmpty()) {
            historyStack.peek().inverse();
            redoStack.push(historyStack.peek());
            historyStack.pop();
        }
        System.out.println("historyStack: " + historyStack.toString());
        System.out.println("redoStack: " + redoStack.toString());
        }

public void redo(){
        if(!redoStack.isEmpty()) {
        redoStack.peek().execute();
        historyStack.push(redoStack.peek());
        redoStack.pop();
        }
        System.out.println("historyStack: " + historyStack.toString());
        System.out.println("redoStack: " + redoStack.toString());
        }
        }
