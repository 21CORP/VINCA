package com.example.a21corp.vinca;

import java.util.List;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class Workspace {
    List<BaseElement> baseElementList;


    //Top-level elment has been added
    //No index provided - append to list
    public boolean addElement(BaseElement element) {
        return baseElementList.add(element);
    }


    //Top-level element added
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
