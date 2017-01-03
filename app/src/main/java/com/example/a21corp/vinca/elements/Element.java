package com.example.a21corp.vinca.elements;

import java.io.Serializable;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class Element extends Container implements Serializable {
    private static final long serialVersionUID = 12345;


    public Element(int elementType) {
        super(elementType);
    }
}
