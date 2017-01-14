package com.example.a21corp.vinca.Editor;

import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.elements.VincaActivity;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 14-01-2017.
 */
public class InputValidator {
    public static boolean moveIsValid(VincaElement child, VincaElement parent) {
        if (child instanceof Node) {
            return (parent instanceof VincaActivity);
        }
        if (child instanceof Element) {
            return (parent instanceof Element);
        }
        return false;
    }
}
