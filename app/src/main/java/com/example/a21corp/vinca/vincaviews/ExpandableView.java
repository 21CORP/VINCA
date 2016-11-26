package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class ExpandableView extends ContainerView {

    public LinearLayout canvas;

    public ExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableView(Context context, int elementType) {
        this(context, null);
        setType(context, elementType, null);
    }

    public void setType(Context context, int elementType, ExpandableView root) {
        if (root == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = (ExpandableView)
                    inflater.inflate(R.layout.expandable_element_view, this, true);
        }
        super.setType(context, elementType, root);

        ImageView borderLeft = (ImageView) root.findViewById(R.id.border_start);
        ImageView borderRight = (ImageView) root.findViewById(R.id.border_end);
        canvas = (LinearLayout) root.findViewById(R.id.canvas);


        switch (elementType) {
            case VincaElement.ELEMENT_PROJECT:
                borderLeft.setImageResource(R.drawable.project_start);
                borderRight.setImageResource(R.drawable.project_end);
                break;
            case VincaElement.ELEMENT_PROCESS:
                borderLeft.setImageResource(R.drawable.process_left);
                borderRight.setImageResource(R.drawable.process_right);
                break;
            case VincaElement.ELEMENT_ITERATE:
                borderLeft.setImageResource(R.drawable.iterate_left);
                borderRight.setImageResource(R.drawable.iterate_right);
                break;
        }
        root.invalidate();
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

}
