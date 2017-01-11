package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.Editor.GhostEditorView;
import com.example.a21corp.vinca.Editor.VincaViewManager;
import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.HistoryManagement.MoveCommand;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 19-11-2016.
 */

public abstract class ContainerView extends LinearLayout implements View.OnClickListener, View.OnDragListener, VincaElementView {

    protected Historian project;
    protected ImageView borderLeft;
    protected ImageView borderRight;
    public Container vincaElement;

    public ContainerView(Context context, Container element) {
        super(context);
        this.vincaElement = element;
        inflate(getContext(), R.layout.expandable_element_view, this);
       onFinishInflate();
    }

    public void add(View v){
        this.addView(v, this.getChildCount()-2);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        borderLeft = (ImageView) findViewById(R.id.border_start);
        borderRight = (ImageView) findViewById(R.id.border_end);
        Log.d("ContainerView", "finished inflating");
    }

    @Override
    public void onClick(View v) {
        project.setCursor(this);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        if(!(v instanceof VincaElementView))
        {
            return false;
        }
        if(event.getAction()==DragEvent.ACTION_DROP)
        {

        }
        return true;
    }

    @Override
    public VincaElement getVincaSymbol() {
        return element;
    }

    @Override
    public void moveHere(ContainerView container) {
        project.move(vincaElement, (Container)container.getVincaSymbol());
    }

    @Override
    public void moveHere(ElementView element) {
        project.move(vincaElement, (ElementView)element.getVincaSymbol());
    }

    @Override
    public void moveHere(NodeView node) {
        project.move(vincaElement, (NodeView)node.getVincaSymbol());
    }

    @Override
    public void add(GhostEditorView view) {
        VincaElement newView = new VincaElement(view.getType());
        project.move(newView, vincaElement);
    }

    @Override
    public void remove() {
        project.remove(vincaElement);
    }
}
