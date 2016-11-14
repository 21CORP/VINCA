package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.example.a21corp.vinca.R;

/**
 * Created by ymuslu on 12-11-2016
 */

public class NodeView extends ElementView {

    public NodeView(Context context, AttributeSet attrs) {
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
        HolderView root = (HolderView)
                inflater.inflate(R.layout.element_view, this, true);

        symbol = (ImageView) root.findViewById(R.id.symbol);

        //Could be an 'if'-statement right now, but will be easier to add Nodes later on like this
        //TODO: Check if switch with 1 case is slower than simple if-statement
        switch (elementType) {
            case ENUM_METHOD:
                symbol.setImageResource(R.drawable.method);
                break;
        }
        //root.setVisibility(VISIBLE);
        root.invalidate();
    }

    public NodeView(Context context) {
        this(context, null);
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
