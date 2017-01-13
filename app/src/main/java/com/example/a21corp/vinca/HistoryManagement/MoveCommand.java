package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaActivity;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by Thomas on 04-01-2017.
 */

public class MoveCommand implements Command {
    VincaElement element;
    Element newParent;
    Element oldParent;
    WorkspaceController workspaceController;
    int newIndex;
    int oldIndex;

    public MoveCommand(VincaElement element, VincaActivity newParent, VincaActivity oldParent
            , int newIndex, int oldIndex, WorkspaceController workspaceController){
        this.newIndex = newIndex;
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
        }
        workspaceController.setParent(element, oldParent, oldIndex);
    }
}
