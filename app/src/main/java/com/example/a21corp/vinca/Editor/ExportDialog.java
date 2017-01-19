package com.example.a21corp.vinca.Editor;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.a21corp.vinca.ImageExporter;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.VincatoSVG;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExportDialog extends DialogFragment {

    private RadioButton svg, png,pdf,file;
    private Activity act;
    private Workspace workspace;
    private View view2;
    private String title;
    private EditText enterTitle;

    public ExportDialog() {
        // Required empty public constructor
    }
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commit();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
        LayoutInflater inflaterz = getActivity().getLayoutInflater();
        final View view = inflaterz.inflate(R.layout.fragment_export_dialog2, null);
        svg = (RadioButton) view.findViewById(R.id.export_svg);
        png = (RadioButton) view.findViewById(R.id.export_png);
        pdf = (RadioButton) view.findViewById(R.id.export_pdf);
        file = (RadioButton) view.findViewById(R.id.export_file);
        enterTitle = (EditText) view.findViewById(R.id.export_name);
        enterTitle.setText(title);

        builder.setView(view);
        builder.setPositiveButton("Export", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (png.isChecked()) {
                    if (act == null) {
                        Log.d("ExportDialog", "Attempting to export before calling setExportTarger()");
                        return;
                    }
                    title = enterTitle.getText().toString();
                    ImageExporter imgexp = new ImageExporter();
                    imgexp.viewToJPG(act,view2 , title);
                    Toast.makeText(getActivity(), "File exported to Gallery", Toast.LENGTH_LONG).show();
                }
                else if(svg.isChecked()){
                    if (act == null) {
                        Log.d("ExportDialog", "Attempting to export before calling setExportTarger()");
                        return;
                    }
                    title = enterTitle.getText().toString();
                    File f = VincatoSVG.getSVGFile(act, workspace.projects, title);
                    Log.d("ExportDialog", "svg checked");
                    if(f == null){
                        Toast.makeText(getActivity(), "Failed to create svg", Toast.LENGTH_LONG).show();;
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Exported to gallery", Toast.LENGTH_LONG).show();
                    }
                }
                else if(file.isChecked()){
                    //export to file
                    Log.d("ExportDialog", "file checked");
                    File dir = Environment.getExternalStorageDirectory();
                    if(!dir.canWrite())
                    {
                        Log.d("Export", "Failed to open extern directory " + Environment.getExternalStorageState());
                        Toast.makeText(getActivity(), "Storage unavailable", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        File exportDir = new File(dir, "VINCA");
                        try
                        {
                            exportDir.mkdir();
                            ProjectManager.saveProject(workspace,exportDir.getAbsolutePath());
                            Toast.makeText(getActivity(), "File saved " + workspace.getTitle(), Toast.LENGTH_LONG).show();
                        }
                        catch(SecurityException e)
                        {
                            Toast.makeText(getActivity(), "Insufficient permissions", Toast.LENGTH_LONG).show();
                        }
                    }


                }
                else if(pdf.isChecked()){
                    //export to pdf
                    Log.d("ExportDialog", "pdf checked");
                }
                dismiss();

            }
        }) .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }

        });
        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                Button negativeButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                //positiveButton.setTextColor(getResources().getColor(R.color.background_material_light_1, null));
                //negativeButton.setTextColor(getResources().getColor(R.color.cancelColor, null));
                positiveButton.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.background_material_light_1));
                negativeButton.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.cancelColor));
            }
        });

        return dialog;
    }


    public void setExportTarget(Activity act, View view, String title, Workspace workspace) {
        this.act = act;
        this.view2 = view;
        this.title = title;
        this.workspace = workspace;
    }
}
