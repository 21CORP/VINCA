package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.Editor.GhostEditorView;
import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.elements.VincaActivity;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by Sebastian on 1/8/2017.
 */

public class ActivityElementView extends ElementView {

    VincaActivity vincaElement;
    GridLayout nodeLine;

    public ActivityElementView(Context context, VincaActivity element, WorkspaceController controller) {
        super(context, element, controller);
        vincaElement = element;
        nodeLine = new GridLayout(context);
        addView(nodeLine);
        nodeLine.setColumnCount(2);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        nodeLine.setClipChildren(false);
        nodeLine.setLayoutParams(lp);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        view.setImageResource(R.drawable.activity);
    }

    public void add(View v){
        int gridSize = ((int)Math.ceil(Math.sqrt(nodeLine.getChildCount() + 1)));
        ImageView image = (ImageView)v;
        image.setMaxHeight(image.getMaxHeight()/8);
        image.setMaxWidth(image.getMaxWidth()/8);
        nodeLine.setColumnCount(gridSize);
        nodeLine.setRowCount(gridSize);
        nodeLine.addView(v);
    }

    @Override
    public VincaActivity getVincaElement() {
        return vincaElement;
    }

    @Override
    public void addGhost(GhostEditorView view) {
        try {
            Node newElement = (Node) VincaElement.create(view.getVincaElement().type);
            project.setParent(newElement, vincaElement, 0);
        } catch (ClassCastException e) {
            super.addGhost(view);
        }
    }
}
