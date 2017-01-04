package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.VincaViewManager;
import com.example.a21corp.vinca.vincaviews.VincaElementView;

/**
 * Created by Thomas on 04-01-2017.
 */

public class EditDescriptionCommand {
    VincaElementView view;
    VincaViewManager manager;
    String title;
    String description;

    public CreateCommand(VincaElementView view, VincaViewManager manager, String title, String description){
        this.view = view;
        this.manager = manager;
        this.title = title;
        this.description = description;
    }

    @Override
    public void execute(){
        //TODO - Fix
    }
}
