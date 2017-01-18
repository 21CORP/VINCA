package com.example.a21corp.vinca.vincaviews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
        if (vincaElement == project.workspace.cursor) {
            project.toggleOpenContainer(vincaElement);
            Log.d("ContainerView", "Container is now " + (vincaElement.isOpen ? "open" : "closed"));
        }
        else {
            project.setCursor(vincaElement);

            System.out.println("Animate!");
            Animation pressAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shakeanim);
            v.startAnimation(pressAnimation);
        }
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
        Container parent = view.getVincaElement();
        int index = parent.containerList.size() - 1;
        index = getIndex(parent, index);
        //project.setParent(vincaElement, parent, index);
        Historian.getInstance().storeAndExecute(new MoveCommand(vincaElement, parent, vincaElement.parent, index, project));
    }

    @Override
    public void setParent(ElementView view) {
        Element element = view.getVincaElement();
        Container parent = element.parent;
        int index = parent.containerList.indexOf(element);
        index = getIndex(parent, index);
        //project.setParent(vincaElement, parent, index);
        Historian.getInstance().storeAndExecute(new MoveCommand(vincaElement, parent, vincaElement.parent, index, project));
    }

    @Override
    public void setParent(NodeView view) {
        Node node = view.getVincaElement();
        VincaActivity nodeParent = node.parent;
        Container parent = nodeParent.parent;
        int index = parent.containerList.indexOf(nodeParent);
        index = getIndex(parent, index);
        //project.setParent(vincaElement, parent, index);
        Historian.getInstance().storeAndExecute(new MoveCommand(vincaElement, parent, vincaElement.parent, index, project));
    }

    private int getIndex(Container parent, int index) {
        if (parent == vincaElement.parent) {
            int oldIndex = parent.containerList.indexOf(vincaElement);
            if ((oldIndex - 1) == index) {
                return index;
            }
            if (oldIndex > index) {
                index = index + 1;
            }
        }
        else {
            index = index + 1;
        }
        return index;
    }

    @Override
    public void addGhost(GhostEditorView view) {
        try {
            Element newElement = (Element) VincaElement.create(view.getVincaElement().type);
            //project.setParent(newElement, vincaElement, vincaElement.containerList.size());
            Historian.getInstance().storeAndExecute(new MoveCommand(newElement, vincaElement, null
                    , vincaElement.containerList.size(), project));
        } catch (ClassCastException e) {
            //Containers may only hold Elements. Attempted to add something else
            e.printStackTrace();
        }
    }

    @Override
    public void remove() {
        Container parent = vincaElement.parent;
        DeleteCommand delCmd = new DeleteCommand(vincaElement, parent, project);
        Historian.getInstance().storeAndExecute(delCmd);
    }

    @Override
    public boolean onLongClick(View v) {
        project.toggleOpenContainer(vincaElement);
        return true;
    }

    public void highlight() {
        setBackgroundResource(R.color.cursorColor);
    }

    public void dehighlight() {
        setBackgroundColor(0);
    }

    private void draghighlight() {
        setBackgroundResource(R.color.dragColor);
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
