package com.example.a21corp.vinca;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.a21corp.vinca.Editor.EditorActivity;


/**
 * Created by Oliver on 04-11-2016.
 */

public class CreateMenuPopUp extends DialogFragment implements View.OnClickListener {

Button b1;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View promptView = inflater.inflate(R.layout.dialogbox, null);
        builder.setView(promptView);

        b1 = (Button) promptView.findViewById(R.id.button4);

      b1.setOnClickListener(this);

        //builder.setTitle("Create new Project")
              builder
                .setPositiveButton("Create project", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent workspace = new Intent(getActivity(), EditorActivity.class);
                        startActivity(workspace);
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // cancel
                    }
                });




        return builder.create();
    }


    @Override
    public void onClick(View view) {
        System.out.println("Import trykt");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivity(intent);
    }
}


