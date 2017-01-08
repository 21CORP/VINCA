package com.example.a21corp.vinca.vincaviews;

import android.content.Context;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Element;

/**
 * Created by Sebastian on 1/8/2017.
 */

public class DecisionElementView extends ElementView {
    public DecisionElementView(Context context, Element element) {
        super(context, element);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        symbol.setImageResource(R.drawable.decision);
    }
}
