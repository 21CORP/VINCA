package com.example.a21corp.vinca.vincaviews;

import android.content.Context;

import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Container;

/**
 * Created by Sebastian on 1/8/2017.
 */

public class ProcessContainerView extends ContainerView {
    public ProcessContainerView(Context context, Container element, Historian histo) {
        super(context, element, histo);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        borderLeft.setImageResource(R.drawable.process_left);
        borderRight.setImageResource(R.drawable.process_right);
    }
}
