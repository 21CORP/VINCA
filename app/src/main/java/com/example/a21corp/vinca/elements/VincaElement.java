package com.example.a21corp.vinca.elements;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ymuslu on 12-11-2016
 */

public class VincaElement {

    public int symbol;
    public int type;
    public String title;
    public String description;
    public Expandable parent;

    public static final int ELEMENT_PROJECT = 0;
    public static final int ELEMENT_PROCESS = 1;
    public static final int ELEMENT_ACTIVITY = 2;
    public static final int ELEMENT_ITERATE = 3;
    public static final int ELEMENT_METHOD = 4;
    public static final int ELEMENT_PAUSE = 5;
    public static final int ELEMENT_DECISION = 6;

    public static final ArrayList<Integer> Expendables =
            new ArrayList<Integer>(Arrays.asList(new Integer[] {
        ELEMENT_PROJECT,
        ELEMENT_PROCESS,
        ELEMENT_ITERATE
    }));

    public static final ArrayList<Integer> Elements =
            new ArrayList<Integer>(Arrays.asList(new Integer[] {
        ELEMENT_ACTIVITY,
        ELEMENT_PAUSE,
        ELEMENT_DECISION
    }));

    public static final ArrayList<Integer> Nodes =
            new ArrayList<Integer>(Arrays.asList(new Integer[] {
        ELEMENT_METHOD
    }));

    public VincaElement(int type) {
        parent = null;
        if (type >= 0) {
            this.type = type;
        }
    }
}
