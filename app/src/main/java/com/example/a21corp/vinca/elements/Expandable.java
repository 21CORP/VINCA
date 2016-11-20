package com.example.a21corp.vinca.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class Expandable extends Element {
    public List<Element> elementList = new ArrayList<Element>();

    public Expandable(int elementType){
        super(elementType);
    }
}
