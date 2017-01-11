package com.example.a21corp.vinca.Editor;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a21corp.vinca.HistoryManagement.CreateCommand;
import com.example.a21corp.vinca.HistoryManagement.DeleteCommand;
import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.HistoryManagement.MoveCommand;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.elements.Expandable;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.vincaviews.ContainerView;
import com.example.a21corp.vinca.vincaviews.VincaElementView;
import com.example.a21corp.vinca.vincaviews.ElementView;
import com.example.a21corp.vinca.vincaviews.NodeView;

/**
 * Created by ymuslu on 17-11-2016.
 */

public class VincaViewManager implements WorkspaceObserver {

    public WorkspaceController workspaceController = new WorkspaceController();
    public Context context;
    private EditorActivity listener;
    private ContainerView cursor;
    private View projectView = null;
    private ContainerView highlightedElement;
    private Historian historian;
    private Workspace workspace;

    public VincaViewManager(Context context, Workspace workspace) {
        this.context = context;
        initViewManager(workspace);
    }

    public VincaViewManager(EditorActivity listener, Workspace workspace) {
        this.listener = listener;
        this.context = listener;
        initViewManager(workspace);
    }

    public void initViewManager(Workspace workspace) {
        historian = Historian.getInstance();
        setWorkspace(workspace);
        workspaceController.observerList.add(this);
        highlightedElement = getCursor();
        highlightView(highlightedElement);
        updateCanvas();
    }

    public View makeViewFromClass(VincaElement element) {

        View view = null;

        int type = element.type;
        if (VincaElement.Expendables.contains(type)) {
            view = makeExpandableView((Expandable) element);
        } else if (VincaElement.Elements.contains(type)) {
            view = makeElementView((Element) element);
        } else if (VincaElement.Nodes.contains(type)) {
            view = makeNodeView((Node) element);
        }
        if (view != null && element != null && element instanceof Container) {

            if (((Container) element).isCursor && view instanceof ContainerView) {
                cursor = (ContainerView)view;
                highlightView((View)view);
            }
            if (projectView == null) {
                projectView = view;
            }
            if (((Container) element).isOpen) {
                createViewForNodesOnContainer((ContainerView) view, (Container) element);
            } else {
                //Vinca Element is closed! Indicate if the element contains anything!
                if (((Container) element).vincaNodeList.size() > 0) {
                    indicateNodesWithin((ContainerView) view, (Container) element);
                }
                if (element instanceof Expandable) {
                    if (((Expandable) element).containerList.size() > 0) {
                        indicateContainersWithin((ContainerView) view);
                    }
                }
            }
            return view;
        }
        Log.d("VincaViewManager", "FATAL ERROR!");
        return null;
    }

    private void indicateContainersWithin(ContainerView view) {
        int height = (int) context.getResources().getDimension(R.dimen.symbol_mid_size);
        int width = (int) context.getResources().getDimension(R.dimen.symbol_mid_size);
        ImageView indicator = new ImageView(context);
        indicator.setImageResource(R.drawable.pause_rotated);
        indicator.setMaxWidth((int) context.getResources().getDimension(R.dimen.symbol_mid_size));
        view.addView(indicator, width, height);
    }

    public void indicateNodesWithin(ContainerView view, Container element) {
        int size = element.vincaNodeList.size();
        int height = (int) context.getResources().getDimension(R.dimen.symbol_small_size);
        int width = (int) context.getResources().getDimension(R.dimen.symbol_small_size);

        ImageView indicator = new ImageView(context);

        indicator.setImageResource(R.drawable.method);

        view.addView(indicator, width, height);

        if (size > 1) {
            TextView textView = new TextView(context);
            String text = "x"+Integer.toString(size);
            textView.setText(text);
            view.addView(textView, width, height);
        }
    }
    private ContainerView makeExpandableView(Expandable element) {

        ContainerView view = new ContainerView(context, element);

        if (element.isOpen) {
            for (VincaElement child : element.containerList) {
                view.add((View)makeViewFromClass(child));
            }
        }

        if (listener != null) {
            setListeners(view);
        }

        return view;
    }

    private ElementView makeElementView(Element element) {
        //TODO: Implement - Elements can contain Node
        int elementType = element.type;

        ElementView view = new ElementView(context, element);
        //view.title.setText(element.title);
        //view.description.setText(element.description);

        if (listener != null) {
            setListeners(view);
        }

        return view;
    }

    private NodeView makeNodeView(Node element) {
        int elementType = element.type;

        NodeView view = new NodeView(context, element);
        //view.title.setText(element.title);
        //view.description.setText(element.description);

        if (listener != null) {
            setListeners(view);
        }

        return view;
    }

    private void createViewForNodesOnContainer(ContainerView view, Container element) {
       /* if (element != null &&
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
        }*/
    }

    public void addElement(VincaElementView elementView) { //TODO Convert to command pattern
        VincaElement element = null;
        CreateCommand cCmd = null;
        if (elementView.getVincaSymbol() == null) {
            if (elementView instanceof ContainerView) {
                element = new Container(VincaElement.ELEMENT_PROCESS);
            } else if (elementView instanceof ElementView) {
                element = new Element(VincaElement.ELEMENT_PAUSE);
            } else if (elementView instanceof NodeView) {
                element = new Node(VincaElement.ELEMENT_METHOD);
            }
            cCmd = new CreateCommand(element, workspaceController);
        } else {
            element = elementView.getVincaSymbol();
            if (element instanceof Expandable) {
                cCmd = new CreateCommand(new Expandable(element.type), workspaceController);
            } else if (element instanceof Element) {
                cCmd = new CreateCommand(new Element(element.type), workspaceController);
            } else if (element instanceof Node) {
                cCmd = new CreateCommand(new Node(element.type), workspaceController);
            }
        }
        historian.storeAndExecute(cCmd);
    }

    public void deleteElement(VincaElementView elementView) {//TODO Convert to command pattern
        if (elementView.getVincaSymbol() != null) {
            VincaElement element = elementView.getVincaSymbol();
            DeleteCommand dCmd = new DeleteCommand(element, element.parent, workspaceController);
            historian.storeAndExecute(dCmd);
        }
    }
    public void moveElement(GhostEditorView newSymbol, VincaElementView parent){

    }
    public void moveElement(VincaElementView elementView, VincaElementView parentView) {//TODO Convert to command pattern
        VincaElement element;
        if (elementView.getVincaSymbol() == null) {
            element = null;
            int type = elementView.getVincaSymbol().type;
            if (VincaElement.Expendables.contains(type)) {
                element = new Expandable(type);
            } else if (VincaElement.Elements.contains(type)) {
                element = new Element(type);
            } else if (VincaElement.Nodes.contains(type)) {
                element = new Node(type);
            }
        } else {
            element = elementView.getVincaSymbol();
        }
        VincaElement parent = parentView.getVincaSymbol();

        MoveCommand mCmd = new MoveCommand(element, parent, element.parent, workspaceController);
        historian.storeAndExecute(mCmd);
    }

    public void setCursor(ContainerView newCursor) {
        if (newCursor.element != null) {
            workspaceController.setCursor(newCursor.element);
            highlightView(newCursor);
            cursor = newCursor;
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

    private void setListeners(View view) {
        view.setOnDragListener(listener);
        view.setOnClickListener(listener);
        view.setOnLongClickListener(listener);
        view.setOnTouchListener(listener);
    }

    @Override
    public void updateCanvas() {
        cursor = null;
        //TODO: Implement multiple projects in a single workspace
        projectView = makeViewFromClass(workspaceController.workspace.projects.get(0));
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

    public void toggleOpenExpandableView(ContainerView view) {
        workspaceController.toggleOpenExpandable((Container) view.element);
    }


    public void setWorkspace(Workspace workspace) {
        workspaceController.setWorkspace(workspace);
    }

    public String getWorkspaceTitle() {
        return workspaceController.workspace.getTitle();
    }

    public void renameWorkspace(String title, String path) {
        workspaceController.renameWorkspace(title, path);
    }
}
