package com.example.a21corp.vinca.elements;
import android.view.View;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class BaseElement {

    public static final int ELEMENT_PROJECT = 0;
    public static final int ELEMENT_PROCESS = 1;
    public static final int ELEMENT_ACTIVITY = 2;
    public static final int ELEMENT_ITERATE = 3;
    public static final int ELEMENT_METHOD = 4;
    public static final int ELEMENT_PAUSE = 5;
    public static final int ELEMENT_DECISION = 6;

    public int icon;
    public int elementID;
    //TODO: 1: Rename, 2: Make custom view
    public View view;


}
