package com.example.a21corp.vinca;

import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;

import com.example.a21corp.vinca.Editor.EditorActivity;
import com.example.a21corp.vinca.Editor.ProjectManager;
import com.example.a21corp.vinca.HistoryManagement.Historian;

import java.io.File;

/**
 * Created by Thomas on 06-01-2017.
 */

public class AutoSaver{

    public CountDownTimer timer;
    private Historian historian;
    private long interval = 5000;

    public AutoSaver(final EditorActivity editorActivity){
        historian = Historian.getInstance();


        timer = new CountDownTimer(interval, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("Autosave","time left: " + (historian.timeSinceChange - millisUntilFinished));
                editorActivity.setSaveStatusBar(false);
            }

            @Override
            public void onFinish() {
                Long deltaTime = (SystemClock.uptimeMillis() - historian.timeSinceChange);
                System.out.println("nowTime: " + SystemClock.uptimeMillis());
                System.out.println("historianTime: " + historian.timeSinceChange);
                if(deltaTime >= interval) {
                    Log.d("Autosave", deltaTime + ", no fun...");
                }
                else{
                    Log.d("Autosave", deltaTime + ", Save time!");
                    editorActivity.setSaveStatusBar(true);
                    //TODO SAVE THE FILE
                }
                timer.start();
            }
        };

    }
}
