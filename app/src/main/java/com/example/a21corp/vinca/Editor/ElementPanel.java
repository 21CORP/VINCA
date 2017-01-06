package com.example.a21corp.vinca.Editor;

import android.view.View;

import com.example.a21corp.vinca.vincaviews.VincaElementView;

/**
 * Created by Thomas on 06-01-2017.
 */

public class ElementPanel implements View.OnClickListener{

    private VincaViewManager viewManager;

    public ElementPanel(VincaViewManager viewManager){
        this.viewManager = viewManager;
    }

    @Override
    public void onClick(View v) {
        viewManager.addElement((VincaElementView) v);
    }
}
