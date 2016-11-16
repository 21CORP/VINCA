package com.example.a21corp.vinca.elements;

import com.example.a21corp.vinca.vincaviews.ExpandableElementView;

import java.util.List;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class Expandable extends Element {
    boolean isOpen;
    //FLAG - Different from classdiagram!
    // Expandable must be able to show an open expandable and its borders (BaseElements)
    List<BaseElement> baseElementList;
    public ExpandableElementView view;


    public boolean addElement(BaseElement element) {
        if (baseElementList.add(element)) {
            view.invalidate();
            return true;
        }
        return false;
    }


    public void addElement(int index, BaseElement element) {
        baseElementList.add(index, element);
        view.invalidate();
    }


    public boolean removeElement(BaseElement element) {
        return baseElementList.remove(element);
    }


    public void moveElement(int newIndex, BaseElement element) {
        baseElementList.remove(element);
        baseElementList.add(newIndex, element);
    }
}
