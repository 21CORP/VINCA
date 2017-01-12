package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by Thomas on 04-01-2017.
 */

public class CreateCommand implements Command{
    VincaElement element;
    WorkspaceController workspaceController;

    public CreateCommand(VincaElement element, WorkspaceController workspaceController){
        this.element = element;
        this.workspaceController = workspaceController;
    }

    @Override
    public void execute(){
        workspaceController.addVincaElement(element);
    }

    @Override
    public void inverse(){
        workspaceController.remove(element);
    }

}
