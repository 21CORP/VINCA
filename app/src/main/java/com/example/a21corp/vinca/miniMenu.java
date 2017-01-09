package com.example.a21corp.vinca;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a21corp.vinca.elements.VincaElement;

public class miniMenu extends Fragment {

    private VincaElement workElement;

    public miniMenu() {
        // Required empty public constructor
    }



    public void setElement(VincaElement element){
        workElement = element;
    }
}
