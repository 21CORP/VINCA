package com.example.a21corp.vinca.Editor;

import android.util.Log;

import com.example.a21corp.vinca.AutoSaver;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.elements.Expandable;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.Node;

import java.io.Serializable;
import java.security.AuthProvider;
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


    public Expandable initiateWorkspace(Expandable project) {
        if (project == null) {
            project = new Expandable(VincaElement.ELEMENT_PROJECT);
        }
        workspace.projects = new ArrayList<Expandable>();
        workspace.projects.add(project);
        setCursor(findCursor(project));
        notifyObservers();
        return project;
    }

    public Expandable initiateWorkspace() {
        return initiateWorkspace(null);
    }

    private Container findCursor(Container scope) {
        Container cursor;
        if (scope.isCursor) {
            setCursor(scope);
            return scope;
        }
        if (scope instanceof Expandable && ((Expandable) scope).containerList != null) {
            for (Container container : ((Expandable) scope).containerList) {
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


    public void setCursor(Container cursor) {
        if (cursor == null) {
            if (workspace.projects.size() == 0) {
                workspace.projects = new ArrayList<Expandable>();
                workspace.projects.add(new Expandable(VincaElement.ELEMENT_PROJECT));
            }
            cursor = workspace.projects.get(0);
        }
        if (workspace.cursor != null) {
            workspace.cursor.isCursor = false;
        }
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
        parent.isOpen = true;
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
        parent.isOpen = true;
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
        if (workspace.projects.remove(element)) {
            //Removed element was a top-level projects
            if (workspace.projects.size() == 0) {
                initiateWorkspace();
                return;
            }
        }
        if (element == workspace.cursor) {
            //Trying to delete the cursor - reset cursor to the main projects symbol
            setCursor(workspace.projects.get(0));
        } else if (element instanceof Container) {
            Container currentCursor = workspace.cursor;
            if (findCursor((Container) element) != null) {
                //Cursor within deleted element - reset cursor to the main projects symbol
                setCursor(workspace.projects.get(0));
            } else {
                setCursor(currentCursor);
            }
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

    public void toggleOpenExpandable(Container element) {
        element.isOpen = !element.isOpen;
        notifyObservers();
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
        notifyObservers();
    }
}
