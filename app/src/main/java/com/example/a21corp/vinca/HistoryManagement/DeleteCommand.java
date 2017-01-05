package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.Editor;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by Thomas on 04-01-2017.
 */

public class DeleteCommand implements Command{
    VincaElement element;
    Editor editor;

    public DeleteCommand(VincaElement element, Editor editor){
        this.element = element;
        this.editor = editor;
    }

    @Override
    public void execute(){
        editor.deleteElement(element);
    }
}
