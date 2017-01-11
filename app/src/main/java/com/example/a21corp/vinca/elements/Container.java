package com.example.a21corp.vinca.elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymuslu on 18-11-2016.
 */

public class Container extends Element implements Serializable {
    private static final long serialVersionUID = 12345;
    public List<Element> containerList = new ArrayList<>();
    //public boolean isCursor;
    public boolean isOpen;
    protected Container(int elementType) {
        super(elementType);
       // isCursor = false;
        isOpen = false;
        parent = null;
    }
}
