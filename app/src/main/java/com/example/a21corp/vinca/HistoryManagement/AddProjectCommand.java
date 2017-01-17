package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 14-01-2017.
 */

public class AddProjectCommand implements Command {
    private int oldIndex;
    private int index;
    private Container parent;
    private Element oldCursor;
    private Container element;
    WorkspaceController workspaceController;

    public AddProjectCommand(Container element, Container parent, int index
            , WorkspaceController workspaceController){
        this.parent = parent;
        this.oldCursor = workspaceController.getCursor();
        this.element = element;
        this.workspaceController = workspaceController;
        this.index = index;
        if (parent != null) {
            oldIndex = parent.containerList.indexOf(element);
            oldIndex = Math.max(oldIndex, workspaceController.workspace.projects.indexOf(element));
        }
        else {
            oldIndex = workspaceController.workspace.projects.size();
        }
    }

    @Override
    public void execute(){
        workspaceController.addProject(element, index);
    }

    @Override
    public void inverse(){
        workspaceController.remove(element);
        if (parent != null) {
            workspaceController.setParent(element, parent, oldIndex);
        }
        workspaceController.setCursor(oldCursor);
    }
}
