package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 12-11-2016
 */

public class ElementView extends ContainerView {

    public ElementView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ElementView(Context context, int elementType) {
        this(context, null);
        setType(context, elementType, null);
    }

    public void setType(Context context, int elementType, ElementView root) {
        if (root == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = (ElementView)
                    inflater.inflate(R.layout.element_view, this, true);
        }
        super.setType(context, elementType, root);

        symbol = (ImageView) root.findViewById(R.id.symbol);

        switch (elementType) {
            case VincaElement.ELEMENT_ACTIVITY:
                symbol.setImageResource(R.drawable.activity);
                break;
            case VincaElement.ELEMENT_PAUSE:
                symbol.setImageResource(R.drawable.pause);
                break;
            case VincaElement.ELEMENT_DECISION:
                symbol.setImageResource(R.drawable.decision);
                break;
        }
        root.invalidate();
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
