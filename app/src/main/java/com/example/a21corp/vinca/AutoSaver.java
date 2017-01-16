package com.example.a21corp.vinca;

import android.app.Fragment;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.a21corp.vinca.Editor.EditorActivity;
import com.example.a21corp.vinca.Editor.ProjectManager;
import com.example.a21corp.vinca.Editor.Workspace;
import com.example.a21corp.vinca.HistoryManagement.Historian;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Thomas on 06-01-2017.
 */

public class AutoSaver{

    private final Workspace workspace;
    private final String dir;
    public CountDownTimer timer;
    private Historian historian;
    private long interval = 5000;
    private boolean started;
    public ArrayList<AutosaveObserver> observerList = new ArrayList<AutosaveObserver>();

    public AutoSaver(final Workspace workspace, final String dir){
        this.workspace = workspace;
        this.dir = dir;
        historian = Historian.getInstance();
        historian.autoSaver = this;
        System.out.println("new Autosaver!");
        timer = new CountDownTimer(interval, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
              //  Log.d("Autosave","time left: " + (historian.timeSinceChange - millisUntilFinished));
            }

            @Override
            public void onFinish() {
                save();
                started = false;
            }
        };
    }

    public void save() {
        ProjectManager.saveProject(workspace, dir);
        notifyObservers(false);
    }

    public void start(){
        if(!started) {
            timer.start();
            started = true;
            notifyObservers(true);
        }
    }

    private void notifyObservers(boolean status) {
        for (AutosaveObserver observer : observerList) {
            observer.saveStatus(status);
        }
    }
}
