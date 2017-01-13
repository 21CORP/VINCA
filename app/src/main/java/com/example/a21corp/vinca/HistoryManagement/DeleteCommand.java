package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by Thomas on 04-01-2017.
 */

public class DeleteCommand implements Command{
    VincaElement element;
    Element oldParent;
    WorkspaceController workspaceController;
    int oldIndex;

    public DeleteCommand(VincaElement element, Element oldParent
            , int oldIndex, WorkspaceController workspaceController){
        this.oldIndex = oldIndex;
        this.element = element;
        this.oldParent = oldParent;
        this.workspaceController = workspaceController;
    }

    @Override
    public void execute(){
        workspaceController.remove(element);
    }

    @Override
    public void inverse(){
        workspaceController.setParent(element, oldParent, oldIndex);
    }
}
