package com.example.a21corp.vinca.Editor;

import com.example.a21corp.vinca.elements.Expandable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Oliver on 04-01-2017.
 */

public class ProjectManager {
    private static ProjectManager instance;
    public ProjectManager(){

    }

    public static ProjectManager getInstance() {
        if (instance == null) {
            instance = new ProjectManager();
        }
        return instance;
    }

    public void createProject(String title) {
        Workspace instance = Workspace.getInstance();
        instance.setTitle(title);
        instance.project = new ArrayList<Expandable>();

    }



    public boolean loadProject(String fileName){

        try {
          Workspace.setWorkspace((Workspace) Serialization.load(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  true;
    }

    public boolean saveProject(String fileName){

        try {
            Serialization.save(Workspace.getInstance(),fileName);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return  false;
    }

    public boolean importProject(File project){
        return  true;
    }
    public boolean exportProject(File project, String state){
        return true;
    }


    public void onProjectChange(){

    }



}
