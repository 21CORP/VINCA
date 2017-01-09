package com.example.a21corp.vinca.Editor;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.a21corp.vinca.ImageExporter;
import com.example.a21corp.vinca.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExportDialog extends DialogFragment implements View.OnClickListener {

    private RadioButton svg, png;
    private Activity act;
    private View view;
    private String title;

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
        View view = inflater.inflate(R.layout.fragment_export_dialog, container, false);

        svg = (RadioButton) view.findViewById(R.id.export_svg);
        png = (RadioButton) view.findViewById(R.id.export_png);
        Button export = (Button) view.findViewById(R.id.button_perform_export);

        export.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (png.isChecked()) {
            if (act == null) {
                Log.d("ExportDialog", "Attempting to export before calling setExportTarger()");
                return;
            }
            ImageExporter imgexp = new ImageExporter();
            imgexp.viewToJPG(act, view, title);
        }
        dismiss();
    }

    public void setExportTarget(Activity act, View view, String title) {
        this.act = act;
        this.view = view;
        this.title = title;
    }
}
