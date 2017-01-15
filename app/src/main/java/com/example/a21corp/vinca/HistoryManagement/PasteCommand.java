package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 15-01-2017.
 */

public class PasteCommand implements Command{
    private Element oldCursor;
    private Element parent;
    private WorkspaceController workspaceController;
    private VincaElement element;
    private int index;

    public PasteCommand(Element parent, int index, WorkspaceController workspaceController){
        this.parent = parent;
        this.workspaceController = workspaceController;
        this.index = index;
        oldCursor = workspaceController.getCursor();
        element = workspaceController.getClipboard();
    }

    @Override
    public void execute(){
        workspaceController.setCursor(oldCursor);
        workspaceController.setParent(element, parent, index);
        //Puts copy of element into clipboard
        workspaceController.setClipboard(element);
    }

    @Override
    public void inverse(){
        //Places oldClipboard as-is into clipboard, rather than copy of oldClipboard
        workspaceController.setClipboardRaw(element);
        workspaceController.remove(element);
        workspaceController.setCursor(oldCursor);
    }
}
