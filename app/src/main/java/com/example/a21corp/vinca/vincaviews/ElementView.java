package com.example.a21corp.vinca.vincaviews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a21corp.vinca.Editor.GhostEditorView;
import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.HistoryManagement.CreateCommand;
import com.example.a21corp.vinca.HistoryManagement.DeleteCommand;
import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.HistoryManagement.MoveCommand;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.element_description;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.elements.VincaActivity;
import com.example.a21corp.vinca.elements.VincaElement;

import static android.R.attr.id;

/**
 * Created by ymuslu on 12-11-2016
 */

public class ElementView extends FrameLayout implements VincaElementView, View.OnDragListener, View.OnClickListener {

    public Element vincaElement;
    protected ImageView view;
    protected WorkspaceController project;
    private RelativeLayout minimenu;
    private ImageButton edit;
    private TextView quickTitle;

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
        /**
        minimenu = (RelativeLayout) findViewById(R.id.miniMenu);
        edit = (ImageButton) findViewById(R.id.editButton);
        edit.setOnClickListener(this);
        quickTitle = (TextView) findViewById(R.id.quickTitle);
         **/
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                View draggedView = (View) event.getLocalState();

                if (draggedView instanceof GhostEditorView) {
                    addGhost((GhostEditorView) draggedView);
                } else if (draggedView instanceof VincaElementView) {
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
    public Element getVincaElement() {
        return vincaElement;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setParent(ContainerView view) {
        Container parent = view.getVincaElement();
        int index = getIndex(parent, parent.containerList.size() - 1);
        Historian.getInstance().storeAndExecute(new MoveCommand(vincaElement, parent, vincaElement.parent, index, vincaElement.parent.containerList.indexOf(vincaElement), project));
    }

    @Override
    public void setParent(ElementView view) {
        Element element = view.getVincaElement();
        Container parent = element.parent;
        int index = getIndex(parent, parent.containerList.indexOf(element));
        Historian.getInstance().storeAndExecute(new MoveCommand(vincaElement, parent, vincaElement.parent, index, vincaElement.parent.containerList.indexOf(vincaElement), project));
    }

    @Override
    public void setParent(NodeView view) {
        Node node = view.getVincaElement();
        VincaActivity nodeParent = node.parent;
        Container parent = nodeParent.parent;
        int index = getIndex(parent, parent.containerList.indexOf(nodeParent));
        Historian.getInstance().storeAndExecute(new MoveCommand(vincaElement, parent, vincaElement.parent, index, vincaElement.parent.containerList.indexOf(vincaElement), project));
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
        Container parent = vincaElement.parent;
        VincaElement newElement = VincaElement.create(view.getVincaElement().type);
        int index = parent.containerList.indexOf(vincaElement) + 1;
        //project.setParent(newElement, parent, index);
        Historian.getInstance().storeAndExecute(new MoveCommand(newElement, parent, newElement.parent, index, null, project));
    }

    @Override
    public void remove() {
        Historian.getInstance().storeAndExecute(new DeleteCommand(vincaElement, vincaElement.parent, vincaElement.parent.containerList.indexOf(vincaElement), project));
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
        } else {
            dehighlight();
        }
    }

    @Override
    public void onClick(View v) {
        project.setCursor(vincaElement);
        System.out.println("Animate!");
        Animation pressAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shakeanim);
        v.startAnimation(pressAnimation);
        /**
        if (v == this) {
            project.setCursor(vincaElement);
            if (minimenu.getVisibility() == VISIBLE) {
                miniMenuVisibility(GONE);
            }
            else {
                miniMenuVisibility(VISIBLE);
            }
        }
        if (v == edit) {
            element_description ed = new element_description();
            ed.setElement(vincaElement);
            ed.show(((Activity) getContext()).getFragmentManager(), "DescriptionWindow");
        }
        **/
    }

    public void miniMenuVisibility(int visibility) {
        System.out.println("minimenu is now: " + visibility);
        minimenu.setVisibility(visibility);
        edit.setVisibility(visibility);
        quickTitle.setVisibility(visibility);
    }
}
