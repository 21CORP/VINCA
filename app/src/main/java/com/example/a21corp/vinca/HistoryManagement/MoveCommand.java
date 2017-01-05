package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.Editor;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by Thomas on 04-01-2017.
 */

public class MoveCommand implements Command {
    VincaElement element;
    VincaElement parentElement;
    Editor editor;

    public MoveCommand(VincaElement element, VincaElement parentElement, Editor editor){
        this.element = element;
        this.parentElement = parentElement;
        this.editor = editor;
    }

    @Override
    public void execute(){
        editor.moveElement(element, parentElement);
    }
}
