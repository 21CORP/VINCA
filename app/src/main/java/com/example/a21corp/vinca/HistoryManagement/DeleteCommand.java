package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by Thomas on 04-01-2017.
 */

public class DeleteCommand implements Command{
    VincaElement element;
    VincaElement oldParent;
    WorkspaceController workspaceController;

    public DeleteCommand(VincaElement element, VincaElement oldParent, WorkspaceController workspaceController){
        this.element = element;
        this.oldParent = oldParent;
        this.workspaceController = workspaceController;
    }

    @Override
    public void execute(){
        workspaceController.deleteElement(element);
    }

    @Override
    public void inverse(){
        workspaceController.moveElement(element, oldParent);
    }
}
