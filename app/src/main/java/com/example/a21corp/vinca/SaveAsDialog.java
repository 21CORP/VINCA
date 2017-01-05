package com.example.a21corp.vinca;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.a21corp.vinca.Editor.EditorActivity;
import com.example.a21corp.vinca.Editor.ProjectManager;
import com.example.a21corp.vinca.Editor.Workspace;

import java.io.File;


public class SaveAsDialog extends DialogFragment {

private EditText newProjName;

     @Override
     public Dialog onCreateDialog(Bundle savedInstanceState) {
         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
         LayoutInflater inflater = getActivity().getLayoutInflater();

         View promptView = inflater.inflate(R.layout.saveas_dialogbox, null);
         builder.setView(promptView);

        newProjName = (EditText) promptView.findViewById(R.id.saveasnewname);
         builder
                 .setPositiveButton("Save as", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                         String pName = newProjName.getText().toString();

                         //To change
                         Workspace.getInstance().setTitle(pName);

                         ProjectManager.getInstance().saveProject(getActivity().getFilesDir().getAbsolutePath()
                                 + File.separator + "workspaces"+"/"
                                 + Workspace.getInstance().getTitle() +".ser");

                         Intent workspace = new Intent(getActivity(), EditorActivity.class);
                         getActivity().finish();
                         startActivity(workspace);


                     }
                 })

                 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                     }

                 });


         return builder.create();
     }


     }
