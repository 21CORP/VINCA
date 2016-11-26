package com.example.a21corp.vinca.Editor;

import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.elements.Expandable;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class Editor {

    public Workspace workspace = new Workspace();
    public List<WorkspaceObserver> observerList = new ArrayList<WorkspaceObserver>();


    public Expandable initiateWorkspace(Expandable project) {
        if (project == null) {
            project = new Expandable(VincaElement.ELEMENT_PROJECT);
        }
        findAndSetCursor(project);
        workspace.project = project;
        notifyObservers();
        return project;
    }

    public Expandable initiateWorkspace() {
        return initiateWorkspace(null);
    }

    private Container findAndSetCursor(Expandable scope) {
        scope.isCursor = true;
        workspace.cursor = scope;
        /**
        if (scope.containerList != null) {
            for (Container container : scope.containerList) {
                if (container.isCursor) {
                    workspace.cursor.isCursor = false;
                    workspace.cursor = container;
                    workspace.cursor.isCursor = true;
                }
            }
        }
         **/
        return workspace.cursor;
    }

    public void setCursor(Container cursor) {
        workspace.cursor.isCursor = false;
        workspace.cursor = cursor;
        cursor.isCursor = true;
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

    private void moveElement(Container container, Container parent) {
        Expandable oldParent = container.parent;
        if (oldParent != null) {
            oldParent.containerList.remove(container);
        }
        if (parent instanceof Expandable) {
            ((Expandable) parent).containerList.add(container);
            container.parent = ((Expandable) parent);
        } else if (parent instanceof Element) {
            Expandable trueParent = parent.parent;
            int position = trueParent.containerList.indexOf(parent) + 1;
            trueParent.containerList.add(position, container);
            container.parent = trueParent;
        }
        notifyObservers();
    }

    private void moveElement(Node element, Container parent) {
        Container oldParent = element.parent;
        if (oldParent != null) {
            oldParent.vincaNodeList.remove(element);
        }
        parent.vincaNodeList.add(element);
        element.parent = parent;
        notifyObservers();
    }

    public void addElement(VincaElement element) {
        if (element instanceof Container) {
            moveElement((Container) element, workspace.cursor);
        } else if (element instanceof Node) {
            moveElement((Node) element, workspace.cursor);
        }
    }

    public void deleteElement(VincaElement element) {
        if (element == workspace.project) {
            //Trying to delete entire project - create a clean nodes
            initiateWorkspace();
            return;
        }
        if (element == workspace.cursor) {
            setCursor(workspace.project);
        }
        if (element instanceof Container) {
            element.parent.containerList.remove(element);
        } else if (element instanceof Node) {
            ((Node) element).parent.vincaNodeList.remove(element);
        }
        notifyObservers();
    }

    private void notifyObservers() {
        for (WorkspaceObserver observer : observerList) {
            observer.updateCanvas();
        }
    }
}
