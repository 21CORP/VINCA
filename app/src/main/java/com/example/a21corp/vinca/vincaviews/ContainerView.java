package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by ymuslu on 19-11-2016.
 */

public class ContainerView extends VincaElementView {

    public LinearLayout nodes;

    public ContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContainerView(Context context, int elementType) {
        this(context, null);
        setType(context, elementType);
    }

    public void setType(Context context, int elementType) {
        type = elementType;
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
