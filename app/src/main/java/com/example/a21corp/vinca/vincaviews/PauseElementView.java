package com.example.a21corp.vinca.vincaviews;

import android.content.Context;

import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Element;

/**
 * Created by Sebastian on 1/8/2017.
 */

public class PauseElementView extends ElementView {


    public PauseElementView(Context context, Element element, WorkspaceController controller) {
        super(context, element, controller);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        symbol.setImageResource(R.drawable.pause);
    }
}
