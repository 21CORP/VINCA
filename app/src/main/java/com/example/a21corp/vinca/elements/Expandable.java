package com.example.a21corp.vinca.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class Expandable extends Container {
    public List<Container> containerList = new ArrayList<Container>();

    public Expandable(int elementType){
        super(elementType);
    }
}
