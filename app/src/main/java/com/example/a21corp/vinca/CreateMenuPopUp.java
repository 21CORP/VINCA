package com.example.a21corp.vinca;

import  android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a21corp.vinca.Editor.EditorActivity;
import com.example.a21corp.vinca.Editor.ProjectManager;
import com.example.a21corp.vinca.Editor.Workspace;

import java.io.File;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Oliver on 04-11-2016.
 */

public class CreateMenuPopUp extends DialogFragment implements View.OnClickListener {

Button b1;
    EditText projectName;
    private String dirPath;
    private File projDir;
    private static final int FILE_SELECT_CODE = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dirPath = getActivity().getFilesDir().getAbsolutePath() + File.separator + "workspaces";
        projDir = new File(dirPath);
        if (!projDir.exists()) {
            projDir.mkdirs();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View promptView = inflater.inflate(R.layout.create_dialogbox, null);
        builder.setView(promptView);

        b1 = (Button) promptView.findViewById(R.id.button4);
        projectName = (EditText) promptView.findViewById(R.id.edittextname);

      b1.setOnClickListener(this);

        //builder.setTitle("Create new Project")
              builder
                .setPositiveButton("Create project", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String pname =  projectName.getText().toString();
                        Workspace workspace = ProjectManager.createProject(pname);
                        if(ProjectManager.inputCheck(workspace.getTitle(), dirPath)) {
                            if (ProjectManager.saveProject(workspace, dirPath)) {
                                Intent editor = new Intent(getActivity(), EditorActivity.class);
                                editor.putExtra("title", pname);
                                startActivity(editor);
                                getActivity().overridePendingTransition( R.anim.slide_up_in, R.anim.slide_up_out);
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


    @Override
    public void onClick(View view) {
        System.out.println("Import trykt");
        importFile();

    }

    public void importFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); //TODO change to "*/*.ser" when we know it works
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(intent, FILE_SELECT_CODE);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILE_SELECT_CODE){
            if(resultCode == RESULT_OK){
                String path = data.getDataString();
                String title = path.substring(path.lastIndexOf("/")+1).replace(".ser", "");
                System.out.println(path);
                System.out.println(title);
                if(ProjectManager.inputCheck(title, dirPath)) {
                    try {
                        ProjectManager.loadProject(data.getDataString()); //TODO useless path
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "File not compatible, please load a VincaApp file", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "There is already a file with that name", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}


