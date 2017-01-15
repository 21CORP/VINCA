package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 14-01-2017.
 */

public class AddProjectCommand implements Command {
    private int index;
    private Container parent;
    private Element oldCursor;
    private Container element;
    WorkspaceController workspaceController;

    public AddProjectCommand(Container element, Container parent
            , WorkspaceController workspaceController){
        this.parent = parent;
        this.oldCursor = workspaceController.getCursor();
        this.element = element;
        this.workspaceController = workspaceController;
        if (parent != null) {
            index = parent.containerList.indexOf(element);
        }
        else {
            index = workspaceController.workspace.projects.size();
        }
    }

    @Override
    public void execute(){
        workspaceController.addProject(element);
    }

    @Override
    public void inverse(){
        workspaceController.remove(element);
        if (parent != null) {
            workspaceController.setParent(element, parent, index);
        }
        workspaceController.setCursor(oldCursor);
    }
}
