package com.example.a21corp.vinca;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a21corp.vinca.Editor.EditorActivity;
import com.example.a21corp.vinca.Editor.ProjectManager;
import com.example.a21corp.vinca.Editor.Workspace;

import java.io.File;


public class SaveAsDialog extends DialogFragment {

private EditText newProjName;
    private Workspace currentWorkspace;
    private String dirPath;
    private File projDir;

     @Override
     public Dialog onCreateDialog(Bundle savedInstanceState) {

        dirPath = getActivity().getFilesDir().getAbsolutePath() + File.separator + "workspaces";
        projDir = new File(dirPath);
        if (!projDir.exists()) {
            projDir.mkdirs();
        }

         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
         LayoutInflater inflater = getActivity().getLayoutInflater();

         View promptView = inflater.inflate(R.layout.saveas_dialogbox, null);
         builder.setView(promptView);

        newProjName = (EditText) promptView.findViewById(R.id.saveasnewname);
         builder
                 .setPositiveButton("Save as", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                         try {
                             if (currentWorkspace == null) {
                                 throw new NullPointerException("Project is null");
                             }
                         } catch (NullPointerException e){
                             e.printStackTrace();
                             return;
                         }
                         String pName = newProjName.getText().toString();
                         Workspace workspace = new Workspace(pName, currentWorkspace.projects);
                         workspace.cursor = currentWorkspace.cursor;

                         if(ProjectManager.inputCheck(workspace.getTitle(), dirPath)) {
                             if (ProjectManager.saveProject(workspace, dirPath)) {
                                 Intent editor = new Intent(getActivity(), EditorActivity.class);
                                 editor.putExtra("title", pName);
                                 getActivity().finish();
                                 startActivity(editor);
                             }
                         }
                         else{
                             Toast.makeText(getActivity().getApplicationContext(), "Please try again with another title", Toast.LENGTH_SHORT).show();
                         }

                     }
                 })

                 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                     }

                 });


         return builder.create();
     }


    public void setCurrentWorkspace(Workspace currentWorkspace) {
        this.currentWorkspace = currentWorkspace;
    }
}
