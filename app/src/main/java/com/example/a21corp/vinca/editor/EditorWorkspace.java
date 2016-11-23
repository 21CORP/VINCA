package com.example.a21corp.vinca.Editor;

import android.content.Context;
import android.util.Log;

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

public class EditorWorkspace {

    private static final EditorWorkspace instance = new EditorWorkspace();
    public Expandable project;
    public Container cursor;
    public List<WorkspaceInterface> observerList = new ArrayList<WorkspaceInterface>();

    private EditorWorkspace() {
        Log.d("EditorWorkspace - Debug", "Creating workspace");
    }

    public static EditorWorkspace getInstance() {
        return instance;
    }

    public Expandable initiateWorkspace(Expandable project) {
        if (project == null) {
            project = new Expandable(VincaElement.ELEMENT_PROJECT);
        }
        findAndSetCursor(project);
        this.project = project;
        notifyObservers();
        return project;
    }

    public Expandable initiateWorkspace() {
        return initiateWorkspace(null);
    }

    private Container findAndSetCursor(Expandable scope) {
        scope.isCursor = true;
        cursor = scope;
        if (scope.containerList != null) {
            for (Container container : scope.containerList) {
                if (container.isCursor) {
                    cursor.isCursor = false;
                    cursor = container;
                    cursor.isCursor = true;
                }
            }
        }
        return cursor;
    }

    public void setCursor(Container cursor) {
        cursor.isCursor = false;
        this.cursor = cursor;
        cursor.isCursor = true;
        notifyObservers();
    }

    public void setParent(VincaElement element, VincaElement parent) {
        if (parent instanceof Container) {
            if (element instanceof Container) {
                setParent((Container) element, (Container) parent);
            } else if (element instanceof Node) {
                setParent((Node) element, (Container) parent);
            }
        }
    }

    public void setParent(Container container, Container parent) {
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

    public void setParent(Node element, Container parent) {
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
            setParent((Container) element, cursor);
        } else if (element instanceof Node) {
            setParent((Node) element, cursor);
        }
    }

    public void deleteElement(VincaElement element) {
        if (element == project) {
            //Trying to delete entire project - create a clean nodes
            initiateWorkspace();
            return;
        }
        if (element == cursor) {
            setCursor(project);
        }
        if (element instanceof Container) {
            element.parent.containerList.remove(element);
        } else if (element instanceof Node) {
            ((Node) element).parent.vincaNodeList.remove(element);
        }
        notifyObservers();
    }

    private void notifyObservers() {
        for (WorkspaceInterface observer : observerList) {
            observer.updateCanvas();
        }
    }
}
