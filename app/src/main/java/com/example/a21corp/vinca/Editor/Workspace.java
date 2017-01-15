package com.example.a21corp.vinca.Editor;

import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ymuslu on 26-11-2016.
 */

public class Workspace implements Serializable {
    private static final long serialVersionUID = 12345;

    private String title;
    public ArrayList<Container> projects = new ArrayList<Container>();
    public Element cursor;
    private VincaElement clipboard;

    public Workspace(String title, ArrayList<Container> projects) {
        this.title = title;
        this.projects = projects;

        if (this.projects.isEmpty() || this.projects.get(0) == null) {
            this.projects = new ArrayList<Container>();
            Container initialProject = (Container) VincaElement.create(VincaElement.ELEMENT_PROJECT);
            this.projects.add(initialProject);
        }

        this.cursor = this.projects.get(0);
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public Element getCursor() {
        if (cursor == null) {
            cursor = projects.get(0);
        }
        return cursor;
    }

    public VincaElement getClipboard() {
        return clipboard;
    }


    public void setClipboard(VincaElement clipboard) {
        this.clipboard = clipboard;
    }
}
