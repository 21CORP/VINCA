package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 12-11-2016
 */

public class NodeView extends FrameLayout implements VincaElementView {
    Node node;
    ImageView image;
    public NodeView(Context context, Node node) {
        super(context);
        this.node = node;
        //inflate(context, R.layout.node_view, this);
        image = (ImageView)ImageView.inflate(getContext(), R.layout.vinca_icon_layout, null);
        onFinishInflate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        image.setImageResource(R.drawable.method);
        Log.d("NodeView", "finished inflating");
    }
    @Override
    public VincaElement getVincaSymbol() {
        return node;
    }

}
