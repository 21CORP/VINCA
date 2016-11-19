package com.example.a21corp.vinca.Editor;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.elements.Expandable;
import com.example.a21corp.vinca.elements.Holder;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.vincaviews.ElementView;
import com.example.a21corp.vinca.vincaviews.VincaElementView;
import com.example.a21corp.vinca.vincaviews.ExpandableView;
import com.example.a21corp.vinca.vincaviews.HolderView;
import com.example.a21corp.vinca.vincaviews.NodeView;

/**
 * Created by ymuslu on 17-11-2016.
 */

public class VINCAViewManager implements WorkspaceInterface {

    public static final EditorWorkspace workspace = new EditorWorkspace();
    public Context context;
    public EditorActivity observer;
    public VincaElementView cursor;
    public VincaElementView projectView = null;

    public VINCAViewManager(Context context) {
        initViewManager();
        this.context = context;
        workspace.observerList.add(this);
    }

    public VINCAViewManager(EditorActivity observer) {
        this.observer = observer;
        this.context = observer;
        initViewManager();
        workspace.observerList.add(this);
    }

    public void initViewManager() {
        if (workspace.project == null) {
            workspace.initiateWorkspace();
        }
        updateCanvas();
    }

    public VincaElementView makeViewFromClass(VincaElement element) {

        VincaElementView view = null;

        int type = element.elementType;
        if (VincaElement.Expendables.contains(type)) {
            view = makeExpandableView((Expandable) element);
        } else if (VincaElement.Holders.contains(type)) {
            view = makeHolderView((Holder) element);
        } else if (VincaElement.Nodes.contains(type)) {
            view = makeNodeView((Node) element);
        }
        if (view != null && element != null && element instanceof Element) {
            view.element = element;
            if (element.isCursor) {
                if (cursor != null) {
                    cursor.setBackgroundResource(0);
                }
                cursor = view;
                cursor.setBackgroundResource(R.color.background_material_light_2);
            }
            if (projectView == null) {
                projectView = view;
            }
            return view;
        }
        Log.d("VINCAViewManager", "FATAL ERROR!");
        return null;
    }

    private ExpandableView makeExpandableView(Expandable element) {
        int elementType = element.elementType;

        ExpandableView view = new ExpandableView(context, elementType);
        LinearLayout elementCanvas = view.canvas;

        for (VincaElement child : element.elementList) {
            elementCanvas.addView(makeViewFromClass(child));
        }

        if (observer != null) {
            view.setOnDragListener(observer);
            view.setOnClickListener(observer);
            view.setOnLongClickListener(observer);
            view.setOnTouchListener(observer);
        }

        return view;
    }

    private HolderView makeHolderView(Holder element) {
        //TODO: Implement - Holders can contain Node
        int elementType = element.elementType;

        HolderView view = new HolderView(context, elementType);
        view.title = element.title;
        view.description = element.description;

        if (observer != null) {
            view.setOnDragListener(observer);
            view.setOnClickListener(observer);
            view.setOnLongClickListener(observer);
            view.setOnTouchListener(observer);
        }

        return view;
    }

    private NodeView makeNodeView(Node element) {
        int elementType = element.elementType;

        NodeView view = new NodeView(context, elementType);
        view.title = element.title;
        view.description = element.description;

        if (observer != null) {
            view.setOnDragListener(observer);
            view.setOnClickListener(observer);
            view.setOnLongClickListener(observer);
            view.setOnTouchListener(observer);
        }

        return view;
    }

    public void addElement(VincaElementView elementView) {
        VincaElement element = elementView.element;
        if (element == null) {
            if (elementView instanceof ExpandableView) {
                element = new Expandable(elementView.type);
            } else if (elementView instanceof HolderView) {
                element = new Holder(elementView.type);
            } else if (elementView instanceof NodeView) {
                element = new Node(elementView.type);
            }
            elementView.element = element;
            workspace.addElement(element);
            return;
        } else if (element instanceof Expandable) {
            workspace.addElement(new Expandable(element.elementType));
        } else if (element instanceof Holder) {
            workspace.addElement(new Holder(element.elementType));
        } else if (element instanceof Node) {
            workspace.addElement(new Node(element.elementType));
        }
    }

    public void deleteElement(VincaElementView elementView) {
        workspace.deleteElement(elementView.element);
    }

    public void moveElement(VincaElementView elementView, VincaElementView parentView) {
        VincaElement element;
        if (elementView.getParent() == observer.elementPanel) {
            element = new VincaElement(elementView.type);
            int type = elementView.type;
            if (VincaElement.Expendables.contains(type)) {
                element = new Expandable(type);
            } else if (VincaElement.Holders.contains(type)) {
                element = new Holder(type);
            } else if (VincaElement.Nodes.contains(type)) {
                element = new Node(type);
            }
        } else {
            element = elementView.element;
        }
        VincaElement parentElement = parentView.element;

        workspace.setParent(element, parentElement);
    }

    public void setCursor(ElementView newCursor) {
        cursor = newCursor;
        if (newCursor.element != null && newCursor.element instanceof Element) {
            workspace.setCursor((Element) newCursor.element);
        }
    }

    public VincaElementView getCursor() {
        if (cursor == null) {
            cursor = projectView;
        }
        return cursor;
    }

    @Override
    public void updateCanvas() {
        projectView = makeViewFromClass(workspace.project);
        if (observer != null) {
            //((ViewGroup) this.canvas.getParent()).removeView(this.canvas);
            observer.canvas.removeAllViews();
            observer.canvas.addView(this.projectView);
            observer.canvas.invalidate();
        }
    }
}
