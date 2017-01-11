package com.example.a21corp.vinca.Editor;

import com.example.a21corp.vinca.elements.Container;
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

    public void setCursor(Container cursor) {
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

    public void moveElement(VincaElement element, VincaElement parent) {
        if (parent instanceof Container) {
            if (element instanceof Container) {
                moveElement((Container) element, (Container) parent);
            } else if (element instanceof Node) {
                moveElement((Node) element, (Container) parent);
            }
        }
    }

    public void moveElement(Container container, Container parent) {
        parent.isOpen = true;
        Container oldParent = container.parent;
        if (oldParent != null) {
            oldParent.containerList.remove(container);
        }
        if (parent instanceof Container) {
            ((Container) parent).containerList.add(container);
            container.parent = ((Container) parent);
        } else if (parent instanceof Element) {
            Container trueParent = parent.parent;
            int position = trueParent.containerList.indexOf(parent) + 1;
            trueParent.containerList.add(position, container);
            container.parent = trueParent;
        }
        notifyObservers();
    }

    public void addElement(VincaElement element) {
        if (element instanceof Container) {
            moveElement((Container) element, workspace.cursor);
        } else if (element instanceof Node) {
            moveElement((Node) element, workspace.cursor);
        }
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
        workspace.setTitle(title);
        if (ProjectManager.saveProject(workspace, path)) {
            ProjectManager.removeProject(oldTitle, path);
        }
    }
    //Generic...
    public void setParent(Node vincaElement, Container parent) {
        return; //You cant add a node to a container
    }
    public void setParent(Element vincaElement, Container parent)
    {
        if(vincaElement.parent!=null)
            vincaElement.parent.containerList.remove(vincaElement);
        parent.containerList.add(vincaElement);
        notifyObservers();
    }
    public void setParent(Container vincaElement, Container parent){
        if(vincaElement.parent!=null)
            vincaElement.parent.containerList.remove(vincaElement);
        parent.containerList.add(vincaElement);
        notifyObservers();
    }
    public void setParent(VincaElement vincaElement, Node node)
    {
        return; //Nodes cannot contain any elements
    }
    public void setParent(VincaElement vincaElement, Container parent)
    {
        if(VincaElement.Elements.contains(vincaElement.type)){
            parent.containerList.add((Element)vincaElement);
        }
        else if(VincaElement.Expendables.contains(vincaElement.type))
        {
            parent.containerList.add((Container)vincaElement);
        }
        // Can't add node to a container
        notifyObservers();
    }

    public void setParent(Container vincaElement, Element parent) {
        return; //Cant add Container to Element
    }

    public void remove(Container vincaElement) {
        vincaElement.parent.containerList.remove(vincaElement);
        notifyObservers();
    }

    public void setParent(Node node, Node vincaSymbol) {
        return; //Nodes cant contain anything
    }

    public void setParent(Node node, Element parent) {

    }

    public void remove(Node node) {
        node.parent.nodes.remove(node);
    }

    public void setParent(VincaElement newView, Element parent) {
        return; //Elements cant contain elements
    }
}
