package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 15-01-2017.
 */

public class CopyCommand implements Command{
    private Element cursor;
    private VincaElement element;
    private VincaElement oldClipboard;
    private WorkspaceController workspaceController;

    public CopyCommand(VincaElement element, WorkspaceController workspaceController){
        this.element = element;
        this.workspaceController = workspaceController;
        this.cursor = workspaceController.getCursor();
        this.oldClipboard = workspaceController.getClipboard();
    }

    @Override
    public void execute(){
        workspaceController.setClipboard(element);
        workspaceController.setCursor(cursor);
    }

    @Override
    public void inverse(){
        workspaceController.setClipboardRaw(oldClipboard);
        workspaceController.setCursor(cursor);
    }
}
