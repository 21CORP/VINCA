package com.example.a21corp.vinca.Editor;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;

/**
 * Created by Oliver on 06-01-2017.
 */

public class ProjectManager2 {
    private  Context context;
    private String dirPath;
    private File projectDir;

    public ProjectManager2(Context context){
        this.context = context;
    }

    public Workspace createProject(){

       /** Workspace workspace = new Workspace();

        return workspace; **/

        return null;

    }
    public void saveProject(Workspace workspace, String projectName){
        try {
            Serialization.save(workspace, dirPath + projectName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public Workspace loadProject(String projectName){

        Object obj = null;
        try {
            obj = Serialization.load(dirPath + projectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Workspace) obj;
    }

    public void createDir(){
        dirPath = context.getFilesDir().getAbsolutePath()
                + File.separator + "workspaces" + File.separator;

        projectDir = new File(dirPath);
    }

}
