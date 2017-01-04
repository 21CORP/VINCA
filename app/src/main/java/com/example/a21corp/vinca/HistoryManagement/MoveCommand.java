package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.VincaViewManager;
import com.example.a21corp.vinca.vincaviews.VincaElementView;

/**
 * Created by Thomas on 04-01-2017.
 */

public class MoveCommand implements Command {
    VincaElementView view;
    VincaElementView parentView;
    VincaViewManager manager;

    public MoveCommand(VincaElementView view, VincaViewManager manager, VincaElementView parentView){
        this.view = view;
        this.parentView = parentView;
        this.manager = manager;
    }

    @Override
    public void execute(){
        manager.moveElement(view, parentView);
    }
}
