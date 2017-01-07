package com.example.a21corp.vinca;

import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;

import com.example.a21corp.vinca.Editor.ProjectManager;
import com.example.a21corp.vinca.HistoryManagement.Historian;

import java.io.File;

/**
 * Created by Thomas on 06-01-2017.
 */

public class AutoSaver{

    public CountDownTimer timer;
    private Historian historian;

    public AutoSaver(final long interval, final String saveFile){
        historian = Historian.getInstance();


        timer = new CountDownTimer(interval*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("Autosave","time left: " + (millisUntilFinished - historian.timeSinceChange)/1000);
            }

            @Override
            public void onFinish() {
                if((SystemClock.uptimeMillis() - historian.timeSinceChange) >= interval*1000) {
                    Log.d("Autosave", (SystemClock.uptimeMillis() - historian.timeSinceChange)/1000 + "no fun...");
                }
                else{
                    Log.d("Autosave","Save time!");
                    ProjectManager.getInstance().saveProject(saveFile);
                }
                timer.start();
            }
        };

    }
}
