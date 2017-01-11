package com.example.a21corp.vinca.vincaviews;

import android.content.Context;

import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Container;

/**
 * Created by Sebastian on 1/8/2017.
 */

public class ProjectContainerView extends ContainerView {


    public ProjectContainerView(Context context, Container element, Historian histo) {
        super(context, element, histo);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        borderLeft.setImageResource(R.drawable.project_start);
        borderRight.setImageResource(R.drawable.project_end);
    }
}
