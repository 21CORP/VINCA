package com.example.a21corp.vinca.HistoryManagement;

import com.example.a21corp.vinca.Editor.VincaViewManager;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.vincaviews.VincaElementView;

/**
 * Created by Thomas on 04-01-2017.
 */

public class EditDescriptionCommand implements Command{
    VincaElement element;
    String title;
    String description;

    public EditDescriptionCommand(VincaElement element, String title, String description){
        this.element = element;
        this.title = title;
        this.description = description;
    }

    @Override
    public void execute(){
        element.title = title;
        element.description = description;
    }
}
