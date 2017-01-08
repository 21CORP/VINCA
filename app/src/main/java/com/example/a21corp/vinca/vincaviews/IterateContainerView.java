package com.example.a21corp.vinca.vincaviews;

import android.content.Context;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Container;

/**
 * Created by Sebastian on 1/8/2017.
 */

public class IterateContainerView extends ContainerView {
    public IterateContainerView(Context context, Container element) {
        super(context, element);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        borderLeft.setImageResource(R.drawable.iterate_left);
        borderRight.setImageResource(R.drawable.iterate_right);
    }
}
