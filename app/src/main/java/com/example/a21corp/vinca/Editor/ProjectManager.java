package com.example.a21corp.vinca.Editor;

import java.io.File;
import java.io.IOException;

/**
 * Created by Oliver on 04-01-2017.
 */

public class ProjectManager {

    public ProjectManager(){

    }

    public int createProject(String title, String description){

        return 0;
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

        return  true;
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
