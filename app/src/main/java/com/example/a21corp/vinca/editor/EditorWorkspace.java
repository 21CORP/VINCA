package com.example.a21corp.vinca.Editor;

import android.util.Log;

import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.elements.Expandable;
import com.example.a21corp.vinca.elements.Holder;
import com.example.a21corp.vinca.elements.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class EditorWorkspace {

    public Expandable project;
    public Element cursor;
    public List<WorkspaceInterface> observerList = new ArrayList<WorkspaceInterface>();

    public EditorWorkspace() {
        Log.d("EditorWorkspace - Debug", "Creating workspace");
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

    private Element findAndSetCursor(Expandable defaultCursor) {
        defaultCursor.isCursor = true;
        cursor = defaultCursor;
        if (defaultCursor.elementList != null) {
            for (Element element : defaultCursor.elementList) {
                if (element.isCursor) {
                    cursor.isCursor = false;
                    cursor = element;
                    cursor.isCursor = true;
                }
            }
        }
        return cursor;
    }

    public void setCursor(Element cursor) {
        cursor.isCursor = false;
        this.cursor = cursor;
        cursor.isCursor = true;
        notifyObservers();
    }

    public void setParent(VincaElement element, VincaElement parent) {
        if (parent instanceof Element) {
            if (element instanceof Element) {
                setParent((Element) element, (Element) parent);
            } else if (element instanceof Node) {
                setParent((Node) element, (Element) parent);
            }
        }
    }

    public void setParent(Element element, Element parent) {
        Expandable oldParent = element.parent;
        if (oldParent != null) {
            oldParent.elementList.remove(element);
        }
        if (parent instanceof Expandable) {
            ((Expandable) parent).elementList.add(element);
            element.parent = ((Expandable) parent);
        } else if (parent instanceof Holder) {
            Expandable trueParent = parent.parent;
            int position = trueParent.elementList.indexOf(parent) + 1;
            trueParent.elementList.add(position, element);
            element.parent = trueParent;
        }
        notifyObservers();
    }

    public void setParent(Node element, Element parent) {
        Element oldParent = element.parent;
        if (oldParent != null) {
            oldParent.vincaNodeList.remove(element);
        }
        parent.vincaNodeList.add(element);
        element.parent = parent;
        notifyObservers();
    }

    public void addElement(VincaElement element) {
        if (element instanceof Element) {
            setParent((Element) element, cursor);
        } else if (element instanceof Node) {
            setParent((Node) element, cursor);
        }
    }

    public void deleteElement(VincaElement element) {
        if (element == project) {
            //Trying to delete entire project - create a clean canvas
            initiateWorkspace();
            return;
        }
        if (element == cursor) {
            element.isCursor = false;
            cursor = project;
            cursor.isCursor = true;
        }
        if (element instanceof Element) {
            project.elementList.remove(element);
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
