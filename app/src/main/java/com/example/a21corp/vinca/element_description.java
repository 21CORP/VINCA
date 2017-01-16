package com.example.a21corp.vinca;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.a21corp.vinca.Editor.EditorActivity;
import com.example.a21corp.vinca.HistoryManagement.EditDescriptionCommand;
import com.example.a21corp.vinca.HistoryManagement.Historian;
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

        View promptView = inflater.inflate(R.layout.fragment_element_description, null);
        titleTV = (TextView) promptView.findViewById(R.id.TitleEditView);
        descTV = (TextView) promptView.findViewById(R.id.DescriptionEditView);

        titleTV.setText(workElement.title);
        descTV.setText(workElement.description);

        builder.setView(promptView);
        builder
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditDescriptionCommand edCmd = new EditDescriptionCommand(workElement, titleTV.getText().toString(), descTV.getText().toString());
                        Historian.getInstance().storeAndExecute(edCmd);
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }

                });
        setRetainInstance(true);
        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                Button negativeButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                if (Build.VERSION.SDK_INT >= 23) {
                    positiveButton.setTextColor(getResources().getColor(R.color.background_material_light_1, null));
                    negativeButton.setTextColor(getResources().getColor(R.color.cancelColor, null));

                }
            }
        });


        return dialog;
    }

    public void setElement(VincaElement element){
        workElement = element;
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }


}
