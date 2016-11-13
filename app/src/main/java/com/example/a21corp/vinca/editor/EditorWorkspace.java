package com.example.a21corp.vinca.editor;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.a21corp.vinca.elements.BaseElement;
import com.example.a21corp.vinca.elements.Expandable;
import com.example.a21corp.vinca.elements.Holder;
import com.example.a21corp.vinca.vincaviews.ElementView;
import com.example.a21corp.vinca.vincaviews.ExpandableElementView;
import com.example.a21corp.vinca.vincaviews.HolderView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class EditorWorkspace {

    public List<BaseElement> baseElementList = new ArrayList<BaseElement>();

    //TODO: Remove - test constuctor
    public EditorWorkspace(Context context) {
        Log.d("EditorWorkspace - Debug", "Creating workspace");

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

    public View addElementToWorkspace(View view, View parent,
                                      int elementType, EditorActivity editorActivity) {
        if (parent == null) {
            parent = EditorActivity.cursor;
        }
        if (view instanceof ElementView) {
            ((ViewGroup) view.getParent()).removeView(view);
            ((ViewGroup) parent).addView(view);
            return view;
        } else {
            BaseElement newElement = null;
            ElementView newView = null;
            if (elementType== BaseElement.ELEMENT_PROJECT
                    || elementType == BaseElement.ELEMENT_PROCESS
                    || elementType == BaseElement.ELEMENT_ITERATE) {
                newElement = new Expandable();
                newView = new ExpandableElementView(editorActivity);
                newView.setType(editorActivity, elementType);
            } else if (elementType >= 0 && elementType <= 6) {
                newElement = new Holder();
                newView = new HolderView(editorActivity);
                newView.setType(editorActivity, elementType);
            }
            newElement.view = newView;
            if (parent instanceof ExpandableElementView) {
                ((ViewGroup) parent.findViewWithTag("canvas")).addView(newView);
            } else if (parent.getTag() != null && parent.getTag() == "canvas") {
                ((ViewGroup) parent).addView(newView);
            } else {
                ((ViewGroup) parent.getParent()).addView(newView);
            }
            newView.setOnDragListener(editorActivity);
            newView.setOnTouchListener(editorActivity);
            newView.setOnClickListener(editorActivity);
            newView.setOnLongClickListener(editorActivity);
            return newView;
        }
    }
}
