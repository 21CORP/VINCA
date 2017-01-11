package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
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
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 12-11-2016
 */

public class ElementView extends FrameLayout implements VincaElementView, View.OnDragListener {

    public Element vincaElement;
    protected ImageView symbol;
    protected WorkspaceController project;
    public ElementView(Context context, Element element, WorkspaceController controller) {
        super(context);
        this.project = controller;
        this.vincaElement = element;
        //inflate(context, R.layout.element_view, this);
        symbol = (ImageView)ImageView.inflate(getContext(), R.layout.vinca_icon_layout, null);
        onFinishInflate();
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        symbol.setImageResource(R.drawable.decision);
        Log.d("ElementView", "finished inflating");
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
                ((VincaElementView) draggedView).setParent(this);
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
        /*Expandable oldParent = vincaElement.parent;
        VincaElement newParent = container.getVincaSymbol();
        MoveCommand move = new MoveCommand(vincaElement, newParent, oldParent, controller);
        move.execute();*/
        project.setParent(vincaElement, (Container)container.getVincaSymbol());
    }

    @Override
    public void setParent(ElementView element) {
        //Trying to setParent Element onto a new Element. Add next to instead
        /*Expandable oldParent = vincaElement.parent;
        VincaElement newParent = element.getVincaSymbol().parent;
        MoveCommand move = new MoveCommand(vincaElement, newParent, oldParent, controller);
        move.execute();*/
        project.setParent(vincaElement, (Element)element.getVincaSymbol());
    }

    @Override
    public void setParent(NodeView node) {
        //Trying to setParent Element onto Node. Add to Node's paren instead
        /*Expandable oldParent = vincaElement.parent;
        VincaElement newParent = node.getVincaSymbol().parent;
        MoveCommand move = new MoveCommand(vincaElement, newParent, oldParent, controller);
        move.execute();*/
        project.setParent(vincaElement, (Node)node.getVincaSymbol());
    }

    @Override
    public void addGhost(GhostEditorView view) {
        VincaElement newView = VincaElement.create(view.getType().type);
        project.setParent(newView, vincaElement);
    }

    @Override
    public void remove() {

    }
}
