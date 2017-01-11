package com.example.a21corp.vinca.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 1/11/17.
 */

public class Activity extends Element {
    public List<Node> nodes = new ArrayList<Node>();

    public Activity(int elementType) {
        super(elementType);
    }
}
