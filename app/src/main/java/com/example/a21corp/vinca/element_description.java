package com.example.a21corp.vinca;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.a21corp.vinca.Editor.EditorActivity;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.vincaviews.VincaElementView;


/**
 * A simple {@link Fragment} subclass.
 */
public class element_description extends DialogFragment{

    VincaElement workElement;
    TextView titleTV;
    TextView descTV;

    public element_description() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View promptView = inflater.inflate(R.layout.element_description, null);
        titleTV = (TextView) promptView.findViewById(R.id.TitleTextView);
        descTV = (TextView) promptView.findViewById(R.id.DescriptionTextView);

        titleTV.setText(workElement.title);
        descTV.setText(workElement.description);

        builder.setView(promptView);
        builder
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        workElement.title = titleTV.getText().toString();
                        workElement.description = descTV.getText().toString();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }

                });

        return builder.create();
    }

    public void setElement(VincaElement element){
        workElement = element;
    }
}
