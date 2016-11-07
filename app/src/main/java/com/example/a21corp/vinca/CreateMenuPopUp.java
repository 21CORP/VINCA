package com.example.a21corp.vinca;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


/**
 * Created by Oliver on 04-11-2016.
 */

public class CreateMenuPopUp extends DialogFragment {

Button b1;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View promptView = inflater.inflate(R.layout.dialogbox, null);


        b1 = (Button) promptView.findViewById(R.id.button2);

      // b1.setOnClickListener();

        //builder.setTitle("Create new Project")
              builder
                .setPositiveButton("Create project", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // open editor
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // cancel
                    }
                });


        builder.setView(promptView);

        return builder.create();
    }




}


