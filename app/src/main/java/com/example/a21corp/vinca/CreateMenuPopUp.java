package com.example.a21corp.vinca;

import android.app.Activity;
import  android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a21corp.vinca.Editor.EditorActivity;
import com.example.a21corp.vinca.Editor.ProjectManager;
import com.example.a21corp.vinca.Editor.Workspace;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;


/**
 * Created by Oliver on 04-11-2016.
 */

public class CreateMenuPopUp extends DialogFragment {

    private static final int WRITE_EXTERNAL_STORAGE_STATE = 255;

    EditText projectName;
    private String dirPath;
    private File projDir;
    public static final int FILE_SELECT_CODE = 1;

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

        projectName = (EditText) promptView.findViewById(R.id.edittextname);

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

                })

                .setNeutralButton("Import", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        importFile();
                    }
                });
        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                Button negativeButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                Button neutralButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEUTRAL);
                if (Build.VERSION.SDK_INT >= 23) {
                    positiveButton.setTextColor(getResources().getColor(R.color.background_material_light_1, null));
                    neutralButton.setTextColor(getResources().getColor(R.color.background_material_light_1, null));
                    negativeButton.setTextColor(getResources().getColor(R.color.cancelColor, null));

                }
            }
        });

        return dialog;
    }

    public void importFile() {
        if (checkPermissionForWriting(getActivity())) {
            Intent getContentIntent = FileUtils.createGetContentIntent();
            Intent intent = Intent.createChooser(getContentIntent, "Select a file");
            getActivity().startActivityForResult(intent, FILE_SELECT_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("CreateMenu", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean checkPermissionForWriting(Activity act) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int permissionCheck =
                    act.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (act.shouldShowRequestPermissionRationale
                        (android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    act.requestPermissions
                            (new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                    , WRITE_EXTERNAL_STORAGE_STATE);
                }
                if (act.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }
}


