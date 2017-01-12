package com.example.a21corp.vinca.Editor;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by Sebastian on 1/9/2017.
 */

public class GhostEditorView extends LinearLayout {
    private VincaElement prototype;
    private int type;
    public GhostEditorView(Context context, VincaElement prototype)
    {
        super(context);
        type = prototype.type;
        this.prototype = prototype;
        initialize();
    }

    protected void initialize() {
        //Not quite how I would like this to function
        ImageView image = (ImageView)ImageView.inflate(getContext(), R.layout.vinca_icon_layout, null);
        this.addView(image);

        switch(prototype.type)
        {
            case VincaElement.ELEMENT_ACTIVITY:
                image.setImageResource(R.drawable.activity);
                break;
            case VincaElement.ELEMENT_DECISION:
                image.setImageResource(R.drawable.decision);
                break;
            case VincaElement.ELEMENT_ITERATE:{
                image.setImageResource(R.drawable.iterate_left);
                ImageView image2 = (ImageView)ImageView.inflate(getContext(), R.layout.vinca_icon_layout, null);
                image2.setImageResource(R.drawable.iterate_right);
                this.addView(image2);
                break;
            }
            case VincaElement.ELEMENT_METHOD:
                image.setImageResource(R.drawable.method);
                break;
            case VincaElement.ELEMENT_PAUSE:
                image.setImageResource(R.drawable.pause);
                break;
            case VincaElement.ELEMENT_PROCESS: {
                image.setImageResource(R.drawable.process_left);
                ImageView image2 = (ImageView)ImageView.inflate(getContext(), R.layout.vinca_icon_layout, null);
                image2.setImageResource(R.drawable.process_right);
                this.addView(image2);
                break;
            }
            case VincaElement.ELEMENT_PROJECT:{
                image.setImageResource(R.drawable.project_start);
                ImageView image2 = (ImageView)ImageView.inflate(getContext(), R.layout.vinca_icon_layout, null);
                image2.setImageResource(R.drawable.project_end);
                this.addView(image2);
                break;
            }
            }
    }
    public VincaElement getVincaElement()
    {
        return VincaElement.create(type);
    }


}
