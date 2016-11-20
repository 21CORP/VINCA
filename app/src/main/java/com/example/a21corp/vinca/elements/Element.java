package com.example.a21corp.vinca.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymuslu on 18-11-2016.
 */

public class Element extends VincaElement {
    boolean isOpen;
    public Expandable parent;
    public List<Node> vincaNodeList = new ArrayList<Node>();

    public Element(int elementType) {
        super(elementType);
        isOpen = false;
        parent = null;
    }
}
