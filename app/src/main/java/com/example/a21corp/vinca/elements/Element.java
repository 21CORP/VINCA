package com.example.a21corp.vinca.elements;

import java.io.Serializable;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class Element extends VincaElement implements Serializable {
    private static final long serialVersionUID = 12345;
    //Pause, Activity, conclusion

    protected Element(int elementType) {
        super(elementType);
    }
}
