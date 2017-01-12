package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.a21corp.vinca.Editor.GhostEditorView;
import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 12-11-2016
 */

public class ElementView extends FrameLayout implements VincaElementView, View.OnDragListener, View.OnClickListener {

    public Element vincaElement;
    protected ImageView view;
    protected WorkspaceController project;
    public ElementView(Context context, Element element, WorkspaceController controller) {
        super(context);
        this.project = controller;
        this.vincaElement = element;
        inflate(context, R.layout.vinca_icon_layout, this);
        view = (ImageView) findViewById(R.id.symbol);
        //view = (ImageView)ImageView.inflate(getContext(), R.layout.vinca_icon_layout, null);
        setOnClickListener(this);
        setOnDragListener(this);
        onFinishInflate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        view.setImageResource(R.drawable.decision);
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
    public VincaElement getVincaElement() {
        return vincaElement;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setParent(ContainerView container) {
        /*Expandable oldParent = vincaElement.parent;
        VincaElement newParent = container.getVincaElement();
        MoveCommand move = new MoveCommand(vincaElement, newParent, oldParent, controller);
        move.execute();*/
        project.setParent(vincaElement, (Container)container.getVincaElement());
    }

    @Override
    public void setParent(ElementView element) {
        //Trying to setParent Element onto a new Element. Add next to instead
        /*Expandable oldParent = vincaElement.parent;
        VincaElement newParent = element.getVincaElement().parent;
        MoveCommand move = new MoveCommand(vincaElement, newParent, oldParent, controller);
        move.execute();*/
        project.setParent(vincaElement, element.getVincaElement().parent);
    }

    @Override
    public void setParent(NodeView node) {
        //Trying to setParent Element onto Node. Add to Node's paren instead
        /*Expandable oldParent = vincaElement.parent;
        VincaElement newParent = node.getVincaElement().parent;
        MoveCommand move = new MoveCommand(vincaElement, newParent, oldParent, controller);
        move.execute();*/
        project.setParent(vincaElement, node.getVincaElement().parent);
    }

    @Override
    public void addGhost(GhostEditorView view) {
        VincaElement newView = VincaElement.create(view.getType().type);
        project.setParent(newView, vincaElement.parent);
    }

    @Override
    public void remove() {

    }

    public void highlight() {
        view.setBackgroundColor(Color.GREEN);
    }

    public void dehighlight() {
        view.setBackgroundColor(0);
    }

    private void draghighlight() {
        view.setBackgroundColor(Color.GRAY);
    }

    private void dragdehighlight() {
        if (project.workspace.getCursor() == vincaElement) {
            highlight();
        }
        else {
            dehighlight();
        }
    }

    @Override
    public void onClick(View v) {
        project.setCursor(vincaElement);
    }
}
