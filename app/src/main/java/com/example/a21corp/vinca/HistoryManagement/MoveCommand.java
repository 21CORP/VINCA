package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaActivity;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by Thomas on 04-01-2017.
 */

public class MoveCommand implements Command {
    Element oldCursor;
    VincaElement element;
    Element newParent;
    Element oldParent;
    int newIndex;
    int oldIndex = -1;
    WorkspaceController workspaceController;

    public MoveCommand(VincaElement element, Element newParent, Element oldParent, int newIndex
            , WorkspaceController workspaceController){
        this.oldCursor = workspaceController.getCursor();
        this.newIndex = newIndex;

        int oldIndex;
        if (oldParent == null) {
            oldIndex = workspaceController.workspace.projects.indexOf(element);
        }
        else {
            oldIndex = element.parent.containerList.indexOf(element);
        }
        this.oldIndex = oldIndex;

        this.element = element;
        this.newParent = newParent;
        this.oldParent = oldParent;
        this.workspaceController = workspaceController;
    }

    @Override
    public void execute(){
        workspaceController.setParent(element, newParent, newIndex);
    }

    @Override
    public void inverse(){
        if(oldParent == null){
            workspaceController.remove(element);
            if (oldIndex > -1) {
                //No parent but an index exists, must previously have been root-level project
                workspaceController.addProject((Container) element, oldIndex);
            }
        }
        else {
            workspaceController.setParent(element, oldParent, oldIndex);
        }
        workspaceController.setCursor(oldCursor);
    }
}
