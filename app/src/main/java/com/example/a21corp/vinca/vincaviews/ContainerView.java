package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.graphics.Color;
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
import com.example.a21corp.vinca.elements.VincaActivity;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 19-11-2016.
 */

public abstract class ContainerView extends LinearLayout implements View.OnClickListener, View.OnDragListener, VincaElementView, View.OnLongClickListener {

    protected WorkspaceController project;
    protected ImageView borderLeft;
    protected ImageView borderRight;
    public Container vincaElement;

    public ContainerView(Context context, Container element, WorkspaceController histo) {
        super(context);
        project = histo;
        this.vincaElement = element;
        setOnClickListener(this);
        setOnDragListener(this);
        setOnLongClickListener(this);
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
    }

    @Override
    public void onClick(View v) {
        project.setCursor(vincaElement);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction())
        {
            case DragEvent.ACTION_DROP:
                View draggedView = (View) event.getLocalState();

                if (draggedView instanceof GhostEditorView) {
                    addGhost((GhostEditorView) draggedView);
                }
                else if (draggedView instanceof VincaElementView) {
                    ((VincaElementView) draggedView).setParent(this);
                }
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                draghighlight();
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                dragdehighlight();
                break;
        }
        return true;
    }

    @Override
    public Container getVincaElement() {
        return vincaElement;
    }

    @Override
    public ContainerView getView() {
        return this;
    }

    @Override
    public void setParent(ContainerView view) {
        Container container = view.getVincaElement();
        int index = container.containerList.size();
        project.setParent(vincaElement, container, index);
    }

    @Override
    public void setParent(ElementView view) {
        Element element = view.getVincaElement();
        Container parent = element.parent;
        int index = parent.containerList.indexOf(element) + 1;
        project.setParent(vincaElement, parent, index);
    }

    @Override
    public void setParent(NodeView view) {
        Node node = view.getVincaElement();
        VincaActivity nodeParent = node.parent;
        Container parent = nodeParent.parent;
        int index = parent.containerList.indexOf(nodeParent) + 1;
        project.setParent(vincaElement, parent, index);
    }

    @Override
    public void addGhost(GhostEditorView view) {
        VincaElement newElement = VincaElement.create(view.getVincaElement().type);
        project.setParent(newElement, vincaElement, vincaElement.containerList.size());
    }

    @Override
    public void remove() {
        project.remove(vincaElement);
    }

    @Override
    public boolean onLongClick(View v) {
        project.toggleOpenContainer(vincaElement);
        return true;
    }

    public void highlight() {
        setBackgroundColor(Color.GREEN);
    }

    public void dehighlight() {
        setBackgroundColor(0);
    }

    private void draghighlight() {
        setBackgroundColor(Color.GRAY);
    }

    private void dragdehighlight() {
        if (project.workspace.getCursor() == vincaElement) {
            highlight();
        }
        else {
            dehighlight();
        }
    }
}
