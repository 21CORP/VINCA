package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by Thomas on 04-01-2017.
 */

public class CreateCommand implements Command{
    Element cursor;
    VincaElement element;
    WorkspaceController workspaceController;

    public CreateCommand(VincaElement element, WorkspaceController workspaceController){
        this.cursor = workspaceController.getCursor();
        this.element = element;
        this.workspaceController = workspaceController;
    }

    @Override
    public void execute(){
        workspaceController.setCursor(cursor);
        workspaceController.addVincaElement(element);
    }

    @Override
    public void inverse(){
        workspaceController.remove(element);
        workspaceController.setCursor(cursor);
    }

}
