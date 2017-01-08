package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 12-11-2016
 */

public class ElementView extends FrameLayout implements VincaElementView{

    public Element element;
    protected ImageView symbol;
    public ElementView(Context context, Element element) {
       super(context);
       this.element = element;
       inflate(context, R.layout.element_view, this);
       onFinishInflate();
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        symbol = (ImageView)findViewById(R.id.symbol);
        symbol.setImageResource(R.drawable.decision);
        Log.d("ElementView", "finished inflating");
    }

    @Override
    public VincaElement getVincaSymbol() {
        return element;
    }
}
