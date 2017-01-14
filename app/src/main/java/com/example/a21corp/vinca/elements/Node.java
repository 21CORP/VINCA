package com.example.a21corp.vinca.elements;


import java.io.Serializable;

/**
 * Created by ymuslu on 13-11-2016.
 */

public class Node extends VincaElement implements Serializable {
    private static final long serialVersionUID = 12345;

    public VincaActivity parent;

    protected Node(int elementType) {
        super(elementType);
    }

    public VincaActivity getParent() {
        return parent;
    }
}
