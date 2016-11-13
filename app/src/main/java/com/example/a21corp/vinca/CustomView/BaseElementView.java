package com.example.a21corp.vinca.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by ymuslu on 13-11-2016.
 */

public class BaseElementView extends LinearLayout {

    protected static final int ENUM_PROJECT = 0;
    protected static final int ENUM_PROCESS = 1;
    protected static final int ENUM_ACTIVITY = 2;
    protected static final int ENUM_ITERATION = 3;
    protected static final int ENUM_METHOD = 4;
    protected static final int ENUM_PAUSE = 5;
    protected static final int ENUM_DECISION = 6;

    public BaseElementView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseElementView(Context context) {
        super(context);
    }

    public void setType(Context context, int elementType) {
        return;
    }
}
