package com.example.a21corp.vinca;

import android.os.CountDownTimer;
import android.os.SystemClock;

import com.example.a21corp.vinca.Editor.ProjectManager;
import com.example.a21corp.vinca.HistoryManagement.Historian;

import java.io.File;

/**
 * Created by Thomas on 06-01-2017.
 */

public class AutoSaver{

    private CountDownTimer timer;
    private Historian historian;

    public AutoSaver(final long interval, final String saveFile){
        historian = Historian.getInstance();


        timer = new CountDownTimer(interval, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("time left: " + (historian.timeSinceChange - millisUntilFinished));
            }

            @Override
            public void onFinish() {
                if((SystemClock.uptimeMillis()/1000 - historian.timeSinceChange) >= interval) {
                    ProjectManager.getInstance().saveProject(saveFile);
                    System.out.println("Save time!");
                }
            }
        };

    }

    public void start(){
        timer.start();
    }

    public void stop(){
        timer.cancel();
    }
}
