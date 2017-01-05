package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.Editor;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by Thomas on 04-01-2017.
 */

public class DeleteCommand implements Command{
    VincaElement element;
    VincaElement oldParent;
    Editor editor;

    public DeleteCommand(VincaElement element, VincaElement oldParent, Editor editor){
        this.element = element;
        this.oldParent = oldParent;
        this.editor = editor;
    }

    @Override
    public void execute(){
        editor.deleteElement(element);
    }

    @Override
    public void inverse(){
        editor.moveElement(element, oldParent);
    }
}
