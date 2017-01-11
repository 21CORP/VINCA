package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.Editor.GhostEditorView;
import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.HistoryManagement.DeleteCommand;
import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.HistoryManagement.MoveCommand;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.Expandable;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 19-11-2016.
 */

public abstract class ContainerView extends LinearLayout implements View.OnClickListener, View.OnDragListener, VincaElementView {

    private final WorkspaceController controller;
    protected ImageView borderLeft;
    protected ImageView borderRight;
    public Container vincaElement;

    public ContainerView(Context context, Container element, WorkspaceController controller) {
        super(context);
        this.controller = controller;
        this.vincaElement = element;
        inflate(getContext(), R.layout.expandable_element_view, this);
       onFinishInflate();
    }

    //TODO: Should be renamed - add is already defined in this class
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
        controller.setCursor(vincaElement);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        if(!(v instanceof VincaElementView))
        {
            return false;
        }
        if(event.getAction()==DragEvent.ACTION_DROP)
        {
            View draggedView = (View) event.getLocalState();

            if (draggedView instanceof GhostEditorView) {
                addGhost((GhostEditorView) draggedView);
            }
            else if (draggedView instanceof VincaElementView) {
                ((VincaElementView) draggedView).moveHere(this);
            }
        }
        return true;
    }

    @Override
    public VincaElement getVincaSymbol() {
        return vincaElement;
    }

    @Override
    public void moveHere(ContainerView container) {
        Expandable oldParent = vincaElement.parent;
        VincaElement newParent = container.getVincaSymbol();
        MoveCommand move = new MoveCommand(vincaElement, newParent, oldParent, controller);
        move.execute();
    }

    @Override
    public void moveHere(ElementView element) {
        //Tried to add Container to an Element. Add to Element's parent instead
        Expandable oldParent = vincaElement.parent;
        VincaElement newParent = element.getVincaSymbol().parent;
        MoveCommand move = new MoveCommand(vincaElement, newParent, oldParent, controller);
        move.execute();
    }

    @Override
    public void moveHere(NodeView node) {
        //Tried to add Container to a Node. Add to Node's parent instead
        Expandable oldParent = vincaElement.parent;
        VincaElement newParent = node.getVincaSymbol().parent;
        MoveCommand move = new MoveCommand(vincaElement, newParent, oldParent, controller);
        move.execute();
    }

    @Override
    public void addGhost(GhostEditorView view) {
        VincaElement ghostView = new VincaElement(view.getType());
        Expandable oldParent = null;
        VincaElement newParent = vincaElement;
        MoveCommand move = new MoveCommand(ghostView, newParent, oldParent, controller);
        move.execute();
    }

    @Override
    public void remove() {
        DeleteCommand delete = new DeleteCommand(vincaElement, vincaElement.parent, controller);
        delete.execute();
    }
}
