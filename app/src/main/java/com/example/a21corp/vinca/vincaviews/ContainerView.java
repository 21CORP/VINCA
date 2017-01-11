package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.Editor.GhostEditorView;
import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 19-11-2016.
 */

public abstract class ContainerView extends LinearLayout implements View.OnClickListener, View.OnDragListener, VincaElementView {

    protected WorkspaceController project;
    protected ImageView borderLeft;
    protected ImageView borderRight;
    public Container vincaElement;

    public ContainerView(Context context, Container element, WorkspaceController histo) {
        super(context);
        project = histo;
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
        project.setCursor(vincaElement);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        if(!(v instanceof VincaElementView))
        {
            return false;
        }
        switch(event.getAction())
        {
            case DragEvent.ACTION_DRAG_ENTERED:
            {

            }
        }
        return true;
    }

    @Override
    public VincaElement getVincaSymbol() {
        return vincaElement;
    }

    @Override
    public void setParent(ContainerView container) {
        project.setParent(vincaElement, (Container)container.getVincaSymbol());
    }

    @Override
    public void setParent(ElementView element) {
        project.setParent(vincaElement, (Element)element.getVincaSymbol());
    }

    @Override
    public void setParent(NodeView node) {
        project.setParent(vincaElement, (Node)node.getVincaSymbol());
    }

    @Override
    public void addGhost(GhostEditorView view) {
        VincaElement newView = VincaElement.create(view.getType().type);
        project.setParent(newView, vincaElement);
    }

    @Override
    public void remove() {
        project.remove(vincaElement);
    }
}
