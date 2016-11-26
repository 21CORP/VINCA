package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.R;

/**
 * Created by ymuslu on 19-11-2016.
 */

public class ContainerView extends VincaElementView {

    public LinearLayout nodes;

    public ContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContainerView(Context context, int elementType) {
        this(context, null);
        setType(context, elementType, null);
    }

    public void setType(Context context, int elementType, ContainerView root) {
        if (root == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = (ElementView)
                    inflater.inflate(R.layout.element_view, this, true);
        }
        super.setType(context, elementType, root);
        nodes = (LinearLayout) root.findViewById(R.id.nodes);
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
