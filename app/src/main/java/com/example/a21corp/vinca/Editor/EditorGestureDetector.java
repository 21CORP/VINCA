package com.example.a21corp.vinca.editor;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by ymuslu on 13-11-2016.
 */

public class EditorGestureDetector extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }
}
