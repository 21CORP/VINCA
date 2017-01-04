package com.example.a21corp.vinca.Editor;

import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Expandable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymuslu on 26-11-2016.
 */

public class Workspace implements Serializable {
    private static final long serialVersionUID = 12345;

    private String title;
    public List<Expandable> project = new ArrayList<Expandable>();
    private static Workspace instance;
    public Container cursor;

    private Workspace() {
    }

    public static Workspace getInstance() {
        if (instance == null) {
            instance = new Workspace();
        }
        return instance;
    }
    public static void setWorkspace(Workspace workspace){
        instance = workspace;

    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


}
