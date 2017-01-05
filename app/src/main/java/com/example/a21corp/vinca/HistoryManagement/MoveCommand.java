package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.Editor;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by Thomas on 04-01-2017.
 */

public class MoveCommand implements Command {
    VincaElement element;
    VincaElement newParent;
    VincaElement oldParent;
    Editor editor;

    public MoveCommand(VincaElement element, VincaElement newParent, VincaElement oldParent, Editor editor){
        this.element = element;
        this.newParent = newParent;
        this.oldParent = oldParent;
        this.editor = editor;
    }

    @Override
    public void execute(){
        editor.moveElement(element, newParent);
    }

    @Override
    public void inverse(){
        if(oldParent == null){
            editor.deleteElement(element);
        }
        editor.moveElement(element, oldParent);
    }
}
