package com.example.a21corp.vinca.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymuslu on 18-11-2016.
 */

public class Container extends VincaElement {
    public boolean isCursor;
    boolean isOpen;
    public List<Node> vincaNodeList = new ArrayList<Node>();

    public Container(int elementType) {
        super(elementType);
        isCursor = false;
        isOpen = false;
        parent = null;
    }
}
