package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 14-01-2017.
 */

public class AddProjectCommand implements Command {
    Element oldCursor;
    Container element;
    WorkspaceController workspaceController;

    public AddProjectCommand(Container element, WorkspaceController workspaceController){
        this.oldCursor = workspaceController.getCursor();
        this.element = element;
        this.workspaceController = workspaceController;
    }

    @Override
    public void execute(){
        workspaceController.addProject(element);
    }

    @Override
    public void inverse(){
        workspaceController.remove(element);
        workspaceController.setCursor(oldCursor);
    }
}
