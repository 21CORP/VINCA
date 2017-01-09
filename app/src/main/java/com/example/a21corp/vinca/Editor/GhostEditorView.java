package com.example.a21corp.vinca.Editor;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by Sebastian on 1/9/2017.
 */

public class GhostEditorView extends LinearLayout {
    private VincaElement prototype;
    public GhostEditorView(Context context, VincaElement prototype)
    {
        super(context);
        this.prototype = prototype;
        initialize();
    }

    protected void initialize() {
        //Not quite how I would like this to function
        ImageView image = new ImageView(getContext());
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
                ImageView image2 = new ImageView(getContext());
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
                ImageView image2 = new ImageView(getContext());
                image2.setImageResource(R.drawable.process_right);
                this.addView(image2);
                break;
            }
            case VincaElement.ELEMENT_PROJECT:{
                image.setImageResource(R.drawable.project_start);
                ImageView image2 = new ImageView(getContext());
                image2.setImageResource(R.drawable.project_end);
                this.addView(image2);
                break;
            }
            }
    }
    public int getType()
    {
        return prototype.type;
    }


}
