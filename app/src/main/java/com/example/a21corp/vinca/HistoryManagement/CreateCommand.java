package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.VincaViewManager;
import com.example.a21corp.vinca.vincaviews.VincaElementView;

/**
 * Created by Thomas on 04-01-2017.
 */

public class CreateCommand implements Command{
    VincaElementView view;
    VincaViewManager manager;

    public CreateCommand(VincaElementView view, VincaViewManager manager){
        this.view = view;
        this.manager = manager;
    }

    @Override
    public void execute(){
        manager.addElement(view);
    }

}
