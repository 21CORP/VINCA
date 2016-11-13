package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.R;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class ExpandableElementView extends ElementView {

    public LinearLayout canvas;

    public ExpandableElementView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExpandableElementView);
        int elementType = array.getInt(R.styleable.ElementView_type, -1);
        array.recycle();

        if (elementType >= 0) {
            setType(context, elementType);
        }
    }

    public void setType(Context context, int elementType) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout root = (LinearLayout)
                inflater.inflate(R.layout.expandable_element_view, this, true);

        ImageView borderLeft = (ImageView) root.findViewById(R.id.border_left);
        ImageView borderRight = (ImageView) root.findViewById(R.id.border_right);
        canvas = (LinearLayout) root.findViewById(R.id.canvas);

        switch (elementType) {
            case ENUM_PROJECT:
                borderLeft.setImageResource(R.drawable.project_start);
                borderRight.setImageResource(R.drawable.project_end);
                break;
            case ENUM_PROCESS:
                borderLeft.setImageResource(R.drawable.process_left);
                borderRight.setImageResource(R.drawable.process_right);
                break;
            case ENUM_ITERATION:
                borderLeft.setImageResource(R.drawable.iterate_left);
                borderRight.setImageResource(R.drawable.iterate_right);
                break;
        }
        //root.setVisibility(VISIBLE);
        root.invalidate();
    }

    public ExpandableElementView(Context context) {
        this(context, null);
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

}
