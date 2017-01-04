package com.example.a21corp.vinca.Editor;

import java.io.File;
import java.io.IOException;

/**
 * Created by Oliver on 04-01-2017.
 */

public class ProjectManager {

public static ProjectManager pm;

    public ProjectManager(){

    }

    public int createProject(String title, String description){

        return 0;
    }

    public boolean loadProject(String fileName, int id){

        try {
            Serialization.load("lol");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  true;
    }

    public boolean saveProject(String fileName, String state ){

        try {
            Serialization.save(Workspace.getInstance());

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
