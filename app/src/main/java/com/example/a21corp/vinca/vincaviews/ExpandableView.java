package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Expandable;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class ExpandableView extends ElementView {

    public LinearLayout canvas;

    public ExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableView(Context context, int elementType) {
        this(context, null);
        setType(context, elementType);
    }

    public void setType(Context context, int elementType) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout root = (LinearLayout)
                inflater.inflate(R.layout.expandable_element_view, this, true);

        ImageView borderLeft = (ImageView) root.findViewById(R.id.border_start);
        ImageView borderRight = (ImageView) root.findViewById(R.id.border_end);
        canvas = (LinearLayout) root.findViewById(R.id.canvas);
        type = elementType;
/**
        TextView title = (TextView) root.findViewById(R.id.element_title);
        TextView description = (TextView) root.findViewById(R.id.element_description);
        title.setTranslationY(title.getBottom() + canvas.getTop());
        title.setTranslationX(canvas.getRight()/2);
        description.setTranslationY(canvas.getBottom() + R.dimen.expandable_element_inner_margin);
        description.setTranslationX(canvas.getRight()/2);
 **/

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
        //root.setVisibility(VISIBLE);
        root.invalidate();
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

}
