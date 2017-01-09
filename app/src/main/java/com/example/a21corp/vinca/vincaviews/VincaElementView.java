package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class VincaElementView extends LinearLayout {

    //public String title, description;
    //public TextView title; <- May be used later for better description UI
    //public TextView description; <- May be used later for better description UI
    public ImageView symbol;
    public VincaElement element;
    public int type;

    public VincaElementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //title = new TextView(context);
        //description = new TextView(context);
    }

    public VincaElementView(Context context, int elementType) {
        this(context, null);
        setType(context, elementType, null);
        //title = new TextView(context);
        //description = new TextView(context);
    }

    public void setType(Context context, int elementType, VincaElementView root) {
        type = elementType;
        return;
    }

    public String getTitle() {
        return element.title;
    }


    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
