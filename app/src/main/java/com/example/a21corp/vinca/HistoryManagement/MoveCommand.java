package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by Thomas on 04-01-2017.
 */

public class MoveCommand implements Command {
    VincaElement element;
    VincaElement newParent;
    VincaElement oldParent;
    WorkspaceController workspaceController;

    public MoveCommand(VincaElement element, VincaElement newParent, VincaElement oldParent, WorkspaceController workspaceController){
        this.element = element;
        this.newParent = newParent;
        this.oldParent = oldParent;
        this.workspaceController = workspaceController;
    }

    @Override
    public void execute(){
        workspaceController.moveElement(element, newParent);
    }

    @Override
    public void inverse(){
        if(oldParent == null){
            workspaceController.deleteElement(element);
        }
        workspaceController.moveElement(element, oldParent);
    }
}
