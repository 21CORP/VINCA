package com.example.a21corp.vinca.Editor;
import android.util.Log;
import com.example.a21corp.vinca.elements.Container;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Oliver on 04-01-2017.
 */

public abstract class ProjectManager {
    private static final String fileExtension = ".ser";


    public static Workspace createProject(String title) {
        Workspace workspace = new Workspace(title, new ArrayList<Container>());
        return workspace;
    }



    public static Workspace loadProject(String absoluteFilePath) throws Exception{
        if (!absoluteFilePath.endsWith(fileExtension)) {
            absoluteFilePath = absoluteFilePath + fileExtension;
        }
        Workspace workspace = (Workspace) Serialization.load(absoluteFilePath);
        String title = absoluteFilePath.substring(absoluteFilePath.lastIndexOf("/") + 1
                ,absoluteFilePath.lastIndexOf(".ser"));
        workspace.setTitle(title);
        return workspace;
    }

    public static boolean saveProject(Workspace workspace, String dirPath){
        String title = workspace.getTitle();
        String filePath = dirPath + "/" + title + fileExtension;
        try {
            Serialization.save(workspace,filePath);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return  true;
    }

    public boolean importProject(File project){
        return  true;
    }
    public boolean exportProject(File project, String state){
        return true;
    }

    public static void removeProject(String title, String dirPath) {
        String absoluteFilePath = dirPath + "/" + title;
        if (!absoluteFilePath.endsWith(fileExtension)) {
            absoluteFilePath = absoluteFilePath + fileExtension;
        }
        File file = new File(absoluteFilePath);
        file.delete();
    }

    public static boolean inputCheck(String input, String dirPath){
        if(input.contains(".") || input.isEmpty()){
            Log.d("Input validation", "Validation failed: Illegal name");
            return false;

        };
        File[] files = new File(dirPath).listFiles();
        for (File file: files) {
            System.out.println(file.getAbsolutePath());
            if(file.getAbsolutePath().endsWith(input + ".ser")){
                Log.d("Input validation", "Validation failed: File already exists");
                return false;
            };
        }
        return true;
    }
}
