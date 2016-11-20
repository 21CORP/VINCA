package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 19-11-2016.
 */

public class ElementView extends VincaElementView {

    public ElementView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ElementView(Context context, int elementType) {
        this(context, null);
        setType(context, elementType);
    }

    public void setType(Context context, int elementType) {
        type = elementType;
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
