package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Holder;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 12-11-2016
 */

public class HolderView extends ElementView {

    public HolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HolderView(Context context, int elementType) {
        this(context, null);
        setType(context, elementType);
    }

    public void setType(Context context, int elementType) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        HolderView root = (HolderView)
                inflater.inflate(R.layout.element_view, this, true);

        symbol = (ImageView) root.findViewById(R.id.symbol);
        type = elementType;

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
        //root.setVisibility(VISIBLE);
        root.invalidate();
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
