package com.example.a21corp.vinca.Editor;

import android.util.Log;

import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.VincaActivity;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.vincaviews.ContainerView;

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
            setParent(element, (Container) cursor, ((Container) cursor).containerList.size());
        }
        else {
            Container parent = cursor.parent;
            int index = parent.containerList.indexOf(cursor) + 1;
            setParent(element, parent, index);
            setCursor(element);
        }
    }

    private boolean addNode(Node node) {
        try {
            VincaActivity parent = (VincaActivity) workspace.getCursor();
            setParent(node, parent, 0);
            return true;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setParent(VincaElement vincaElement, Element parent, int index) {
        if (parent == null && vincaElement.type == VincaElement.ELEMENT_PROJECT) {
            addProject((Container) vincaElement, index);
        }
        if (vincaElement instanceof Element && parent instanceof Container) {
            setParent((Element) vincaElement, (Container) parent, index);
        }
        else if (vincaElement instanceof Node && parent instanceof VincaActivity) {
            setParent((Node) vincaElement, (VincaActivity) parent, index);
        }
        else {
            Log.d("WorkspaceController", "Illegal operation attempted!");
        }
        notifyObservers();
    }

    private void setParent(Element element, Container parent, int index)
    {
        remove(element);
        parent.containerList.add(index, element);
        element.parent = parent;
    }

    private void setParent(Node node, VincaActivity parent, int index) {
        if (node.parent != null) {
            node.parent.nodes.remove(node);
        }
        parent.nodes.add(index, node);
        node.parent = parent;
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
        //This call will do nothing and return false if vincaElement is not in the list
        if (workspace.projects.remove(vincaElement)) {
            //The deleted element was a root-element
            if (workspace.projects.isEmpty()) {
                initiateWorkspace();
            }
        }
        else {
            if (vincaElement.getParent() != null) {
                vincaElement.parent.containerList.remove(vincaElement);
            }
        }
    }

    private void remove(Node node) {
        node.parent.nodes.remove(node);
    }

    public String getWorkspaceTitle() {
        return workspace.getTitle();
    }

    public void addProject(Container element, int index) {
        remove(element);
        workspace.projects.add(index, element);
        notifyObservers();
    }

    public void addProject(Container element) {
        addProject(element, workspace.projects.size());
    }

    public Element getCursor() {
        return workspace.getCursor();
    }

    public VincaElement getClipboard() {
        return workspace.getClipboard();
    }

    public VincaElement setClipboard(VincaElement element) {
        VincaElement clone = VincaElement.makeCopy(element);
        workspace.setClipboard(clone);
        return clone;
    }

    public void setClipboardRaw(VincaElement element) {
        workspace.setClipboard(element);
    }
}
