package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 12-11-2016
 */

public class NodeView extends VincaElementView {

    public NodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NodeView(Context context, int elementType) {
        this(context, null);
        setType(context, elementType, null);
    }

    public void setType(Context context, int elementType, VincaElementView root) {
        if (root == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = (NodeView)
                    inflater.inflate(R.layout.node_view, this, true);
        }
        super.setType(context, elementType, root);

        symbol = (ImageView) root.findViewById(R.id.symbol);

        switch (elementType) {
            case VincaElement.ELEMENT_METHOD:
                symbol.setImageResource(R.drawable.method);
                break;
        }
        root.invalidate();
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
