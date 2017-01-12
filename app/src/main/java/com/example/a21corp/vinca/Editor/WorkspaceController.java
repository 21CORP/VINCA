package com.example.a21corp.vinca.Editor;

import android.util.Log;

import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.VincaActivity;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class WorkspaceController implements Serializable {

    private static final long serialVersionUID = 12345;

    public Workspace workspace;
    public transient List<WorkspaceObserver> observerList = new ArrayList<WorkspaceObserver>();

    public WorkspaceController() {}

    public WorkspaceController(Workspace workspace) {
        this.workspace = workspace;
    }


    public Container initiateWorkspace(Container project) {
        if (project == null) {
            project = (Container) VincaElement.create(VincaElement.ELEMENT_PROJECT);
        }
        workspace.projects = new ArrayList<Container>();
        workspace.projects.add(project);
       // setCursor(findCursor(project));
        notifyObservers();
        return project;
    }

    public Container initiateWorkspace() {
        return initiateWorkspace(null);
    }
/*
    private Container findCursor(Container scope) {
        Container cursor;
        if (scope.isCursor) {
            setCursor(scope);
            return scope;
        }
        if (scope instanceof Container && ((Container) scope).containerList != null) {
            for (Container container : ((Container) scope).containerList) {
                if (container.isCursor) {
                    setCursor(container);
                    return container;
                } else {
                    cursor = findCursor(container);
                    if (cursor != null) {
                        setCursor(cursor);
                        return cursor;
                    }
                }
            }
        }
        return null;
    }
*/

    public void setCursor(Element cursor) {
        if (cursor == null) {
            if (workspace.projects.size() == 0) {
                workspace.projects = new ArrayList<Container>();
                //workspace.projects.add(new Container(VincaElement.ELEMENT_PROJECT));
            }
            cursor = workspace.projects.get(0);
        }
        if (workspace.cursor != null) {
           // workspace.cursor.isCursor = false;
        }
        workspace.cursor = cursor;
       // cursor.isCursor = true;
        notifyObservers();
    }
    
    private void notifyObservers() {
        for (WorkspaceObserver observer : observerList) {
            observer.updateCanvas();
        }
    }

    public void toggleOpenContainer(Container element) {
        element.isOpen = !element.isOpen;
        notifyObservers();
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
        notifyObservers();
    }

    public void renameWorkspace(String title, String path) {
        String oldTitle = workspace.getTitle();
        if (title == oldTitle) {
            return;
        }
        workspace.setTitle(title);
        if (ProjectManager.saveProject(workspace, path)) {
            ProjectManager.removeProject(oldTitle, path);
        }
    }

    public void addVincaElement(VincaElement element) {
        if (element instanceof Node) {
            addNode((Node) element);
        }
        if (element instanceof Element) {
            addElement((Element) element);
        }
        notifyObservers();
    }

    private void addElement(Element element) {
        VincaElement cursor = workspace.getCursor();
        if (cursor instanceof Container) {
            setParent(element, (Container) cursor);
        }
        else {
            Container parent = cursor.parent;
            setParent(element, parent);
        }
    }

    private boolean addNode(Node node) {
        try {
            VincaActivity parent = (VincaActivity) workspace.getCursor();
            setParent(node, parent);
            return true;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setParent(VincaElement vincaElement, Element parent) {
        if (vincaElement instanceof Element && parent instanceof Container) {
            setParent((Element) vincaElement, (Container) parent);
        }
        else if (parent instanceof VincaActivity) {
            setParent((Node) vincaElement, (VincaActivity) parent);
        }
        notifyObservers();
    }

    private void setParent(Element element, Container parent)
    {
        if(element.parent!=null)
            element.parent.containerList.remove(element);
        parent.containerList.add(element);
    }

    private void setParent(Node node, VincaActivity parent) {
        if (node.parent != null) {
            node.parent.nodes.remove(node);
        }
        parent.nodes.remove(node);
    }

    public void remove(VincaElement vincaElement) {
        if (vincaElement instanceof Node) {
            remove((Node) vincaElement);
        }
        else {
            remove((Element) vincaElement);
        }
        notifyObservers();
    }

    private void remove(Element vincaElement) {
        vincaElement.parent.containerList.remove(vincaElement);
    }

    private void remove(Node node) {
        node.parent.nodes.remove(node);
    }
}
