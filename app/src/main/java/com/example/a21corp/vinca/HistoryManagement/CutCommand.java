package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 15-01-2017.
 */

public class CutCommand implements Command{
    private Element cursor;
    private VincaElement element;
    private Element oldParent;
    private WorkspaceController workspaceController;
    private VincaElement oldClipboard;
    int oldIndex;

    public CutCommand(VincaElement element, Element oldParent
            , WorkspaceController workspaceController){
        this.element = element;
        this.oldParent = oldParent;
        this.workspaceController = workspaceController;
        oldClipboard = workspaceController.getClipboard();
        cursor = workspaceController.getCursor();
    }

    @Override
    public void execute(){
        workspaceController.setClipboard(element);
        workspaceController.remove(element);
        workspaceController.setCursor(cursor);
    }

    @Override
    public void inverse(){
        workspaceController.setCursor(cursor);
        workspaceController.setParent(element, oldParent, oldIndex);
        workspaceController.setClipboardRaw(oldClipboard);
    }
}