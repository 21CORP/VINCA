package com.example.a21corp.vinca.Editor;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
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
public class ExportDialog extends DialogFragment implements View.OnClickListener {

    private RadioButton svg, png,pdf,file;
    private Activity act;
    private Workspace workspace;
    private View view;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_export_dialog2, container, false);

        svg = (RadioButton) view.findViewById(R.id.export_svg);
        png = (RadioButton) view.findViewById(R.id.export_png);
        pdf = (RadioButton) view.findViewById(R.id.export_pdf);
        file = (RadioButton) view.findViewById(R.id.export_file);
        Button export = (Button) view.findViewById(R.id.button_perform_export);
        enterTitle = (EditText) view.findViewById(R.id.export_name);

        export.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enterTitle.setText(title);
    }

    @Override
    public void onClick(View v) {

    //switch case here?

        if (png.isChecked()) {
            if (act == null) {
                Log.d("ExportDialog", "Attempting to export before calling setExportTarger()");
                return;
            }
            title = enterTitle.getText().toString();
            ImageExporter imgexp = new ImageExporter();
            imgexp.viewToJPG(act, view, title);
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

    public void setExportTarget(Activity act, View view, String title, Workspace workspace) {
        this.act = act;
        this.view = view;
        this.title = title;
        this.workspace = workspace;
    }
}
