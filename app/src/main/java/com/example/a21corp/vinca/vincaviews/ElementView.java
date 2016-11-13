package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.R;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class ElementView extends LinearLayout {

    protected static final int ENUM_PROJECT = 0;
    protected static final int ENUM_PROCESS = 1;
    protected static final int ENUM_ACTIVITY = 2;
    protected static final int ENUM_ITERATION = 3;
    protected static final int ENUM_METHOD = 4;
    protected static final int ENUM_PAUSE = 5;
    protected static final int ENUM_DECISION = 6;

    public void setType(Context context, int elementType) {
        return;
    }

    public String title, description;
    public ImageView symbol;

    public ElementView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExpandableElementView);
        int elementType = array.getInt(R.styleable.ElementView_type, -1);
        array.recycle();

        if (elementType >= 0) {
            setType(context, elementType);
        }
    }



    public ElementView(Context context) {
        this(context, null);
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
