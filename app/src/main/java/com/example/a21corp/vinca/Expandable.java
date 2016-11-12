package com.example.a21corp.vinca;

import java.util.List;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class Expandable extends Element {
    boolean isOpen;
    //FLAG - Different from classdiagram!
    // Expandable must be able to show an open expandable and its borders (BaseElements)
    List<BaseElement> baseElementList;


    public boolean addElement(BaseElement element) {
        return baseElementList.add(element);
    }


    public void addElement(int index, BaseElement element) {
        baseElementList.add(index, element);
    }


    public boolean removeElement(BaseElement element) {
        return baseElementList.remove(element);
    }


    public void moveElement(int newIndex, BaseElement element) {
        baseElementList.remove(element);
        baseElementList.add(newIndex, element);
    }
}
