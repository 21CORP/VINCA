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
import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.HistoryManagement.MoveCommand;
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
        int columns = ((int)Math.ceil(Math.sqrt(nodeLine.getChildCount() + 1)));
        int rows = columns > 2 ? 2 : columns;
        ImageView image = (ImageView)v;
        int scaleX = image.getMaxHeight()/rows;
        int scaleY = image.getMaxWidth()/rows;
        image.setMaxHeight(scaleX);
        image.setMaxWidth(scaleY);
        if(nodeLine.getChildCount() > 0)
        {
            ((ImageView)nodeLine.getChildAt(0)).setMaxHeight(scaleY);
            ((ImageView)nodeLine.getChildAt(0)).setMaxWidth(scaleX);

        }
        nodeLine.setColumnCount(columns);
        nodeLine.setRowCount(rows);
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
            Historian.getInstance().storeAndExecute(new MoveCommand(newElement
                    , vincaElement, null, vincaElement.nodes.size(), null, project));
        } catch (ClassCastException e) {
            super.addGhost(view);
        }
    }
}
