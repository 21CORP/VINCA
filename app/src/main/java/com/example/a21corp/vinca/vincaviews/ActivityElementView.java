package com.example.a21corp.vinca.vincaviews;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Element;

/**
 * Created by Sebastian on 1/8/2017.
 */

public class ActivityElementView extends ElementView {
    public ActivityElementView(Context context, Element activity) {
        super(context, activity);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        symbol.setImageResource(R.drawable.activity);
    }
}
