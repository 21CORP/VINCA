package com.example.a21corp.vinca.Entitiy;

import android.content.Context;
import android.util.Log;

import com.example.a21corp.vinca.CustomView.ExpandableElementView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class Workspace {

    public List<BaseElement> baseElementList = new ArrayList<BaseElement>();

    //TODO: Remove - test constuctor
    public Workspace(Context context) {
        Log.d("Workspace - Debug", "Creating workspace");

        Expandable initProject = new Expandable();
        ExpandableElementView newView = new ExpandableElementView(context);
        newView.setType(context, BaseElement.ELEMENT_PROJECT);
        initProject.view = newView;
        addElement(initProject);
    }


    //Top-level elment has been added
    //No index provided - append to list
    public void addElement(BaseElement element) {
        baseElementList.add(element);
    }


    //Top-level element added
    public void addElement(int index, BaseElement element) {
    }


    public void removeElement(BaseElement element) {
    }


    public void moveElement(int newIndex, BaseElement element) {
    }
}
