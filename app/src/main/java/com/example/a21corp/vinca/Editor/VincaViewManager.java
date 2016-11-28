package com.example.a21corp.vinca.Editor;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.elements.Expandable;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.vincaviews.ContainerView;
import com.example.a21corp.vinca.vincaviews.VincaElementView;
import com.example.a21corp.vinca.vincaviews.ExpandableView;
import com.example.a21corp.vinca.vincaviews.ElementView;
import com.example.a21corp.vinca.vincaviews.NodeView;

/**
 * Created by ymuslu on 17-11-2016.
 */

public class VincaViewManager implements WorkspaceObserver {

    private Editor editor;
    public Context context;
    private EditorActivity listener;
    private VincaElementView cursor;
    private VincaElementView projectView = null;
    private ContainerView highlightedElement;

    public VincaViewManager(Context context) {
        this.context = context;
        initViewManager();
    }

    public VincaViewManager(EditorActivity listener) {
        this.listener = listener;
        this.context = listener;
        initViewManager();
    }

    public void initViewManager() {
        editor = new Editor();
        if (Workspace.project == null) {
            editor.initiateWorkspace();
        }
        editor.observerList.add(this);
        highlightedElement = getCursor();
        highlightView(highlightedElement);
        updateCanvas();
    }

    public VincaElementView makeViewFromClass(VincaElement element) {

        VincaElementView view = null;

        int type = element.type;
        if (VincaElement.Expendables.contains(type)) {
            view = makeExpandableView((Expandable) element);
        } else if (VincaElement.Elements.contains(type)) {
            view = makeElementView((Element) element);
        } else if (VincaElement.Nodes.contains(type)) {
            view = makeNodeView((Node) element);
        }
        if (view != null && element != null && element instanceof Container) {
            view.element = element;

            if (((Container) element).isCursor) {
                cursor = view;
                highlightView(view);
            }
            if (projectView == null) {
                projectView = view;
            }
            if (element instanceof Container) {
                createViewForNodesOnContainer((ContainerView) view, (Container) element);
            }
            return view;
        }
        Log.d("VincaViewManager", "FATAL ERROR!");
        return null;
    }

    private ExpandableView makeExpandableView(Expandable element) {
        int elementType = element.type;

        ExpandableView view = new ExpandableView(context, elementType);
        LinearLayout elementCanvas = view.canvas;

        for (VincaElement child : element.containerList) {
            elementCanvas.addView(makeViewFromClass(child));
        }

        if (listener != null) {
            setListeners(view);
        }

        return view;
    }

    private ElementView makeElementView(Element element) {
        //TODO: Implement - Elements can contain Node
        int elementType = element.type;

        ElementView view = new ElementView(context, elementType);
        view.title = element.title;
        view.description = element.description;

        if (listener != null) {
            setListeners(view);
        }

        return view;
    }

    private NodeView makeNodeView(Node element) {
        int elementType = element.type;

        NodeView view = new NodeView(context, elementType);
        view.title = element.title;
        view.description = element.description;

        if (listener != null) {
            setListeners(view);
        }

        return view;
    }

    private void createViewForNodesOnContainer(ContainerView view, Container element) {
        if (element != null &&
            !(element).vincaNodeList.isEmpty()) {
            switch (view.type) {
                case VincaElement.ELEMENT_ACTIVITY:
                    break;
                case VincaElement.ELEMENT_DECISION | VincaElement.ELEMENT_PAUSE:
                    ImageView symbol = view.symbol;
                    ViewGroup symbolParent = (ViewGroup) symbol.getParent();
                    symbolParent.removeView(symbol);
                    symbolParent.addView(symbol);
                    break;
            }
            for (int i = 0; i < element.vincaNodeList.size(); i++) {
                Node node = element.vincaNodeList.get(i);
                NodeView nodeView = makeNodeView(node);
                nodeView.element = node;

                nodeView.symbol.getLayoutParams().width
                        = (int) context.getResources().getDimension(R.dimen.symbol_small_size);
                nodeView.symbol.getLayoutParams().height
                        = (int) context.getResources().getDimension(R.dimen.symbol_small_size);

                view.nodes.addView(nodeView);
                setListeners(nodeView);
                nodeView.setOnDragListener(null);
            }
        }
    }

    public void addElement(VincaElementView elementView) {
        VincaElement element = null;
        if (elementView.element == null) {
            if (elementView instanceof ExpandableView) {
                element = new Expandable(elementView.type);
            } else if (elementView instanceof ElementView) {
                element = new Element(elementView.type);
            } else if (elementView instanceof NodeView) {
                element = new Node(elementView.type);
            }
            editor.addElement(element);
            return;
        } else {
            element = elementView.element;
            if (element instanceof Expandable) {
                editor.addElement(new Expandable(element.type));
            } else if (element instanceof Element) {
                editor.addElement(new Element(element.type));
            } else if (element instanceof Node) {
                editor.addElement(new Node(element.type));
            }
        }
    }

    public void deleteElement(VincaElementView elementView) {
        VincaElement element = elementView.element;
        editor.deleteElement(element);
    }

    public void moveElement(VincaElementView elementView, VincaElementView parentView) {
        VincaElement element;
        if (elementView.getParent() == listener.elementPanel) {
            element = null;
            int type = elementView.type;
            if (VincaElement.Expendables.contains(type)) {
                element = new Expandable(type);
            } else if (VincaElement.Elements.contains(type)) {
                element = new Element(type);
            } else if (VincaElement.Nodes.contains(type)) {
                element = new Node(type);
            }
        } else {
            element = elementView.element;
        }
        VincaElement parent = parentView.element;

        editor.moveElement(element, parent);
    }

    public void setCursor(ContainerView newCursor) {
        VincaElementView oldCursor = cursor;
        cursor = newCursor;
        if (newCursor.element != null && newCursor.element instanceof Container) {
            editor.setCursor((Container) newCursor.element);
            highlightView(cursor);
        } else {
            cursor = oldCursor;
        }
    }

    public ContainerView getCursor() {
        if (cursor instanceof ContainerView) {
            return (ContainerView) cursor;
        } else if (cursor == null && projectView instanceof ContainerView) {
            setCursor( (ContainerView) projectView);
            return (ContainerView) projectView;
        } else {
            return null;
        }
    }

    private void setListeners(VincaElementView view) {
        view.setOnDragListener(listener);
        view.setOnClickListener(listener);
        view.setOnLongClickListener(listener);
        view.setOnTouchListener(listener);
    }

    @Override
    public void updateCanvas() {
        cursor = null;
        projectView = makeViewFromClass(Workspace.project);
        if (listener != null) {
            //((ViewGroup) this.nodes.getParent()).removeView(this.nodes);
            listener.canvas.removeAllViews();
            listener.canvas.addView(this.projectView);
            listener.canvas.invalidate();
            getCursor();
            highlightView(cursor);
        }
    }

    public void highlightView(View view) {
        if (view instanceof ContainerView) {
            if (highlightedElement != null) {
                highlightedElement.setBackgroundResource(0);
            }
            highlightedElement = ((ContainerView) view);
            if (highlightedElement != null) {
                highlightedElement.setBackgroundResource(R.drawable.highlight);
            }
        }
    }
}
