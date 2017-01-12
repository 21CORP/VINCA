package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Element;

/**
 * Created by Sebastian on 1/8/2017.
 */

public class ActivityElementView extends ElementView {

    LinearLayout nodeLine;

    public ActivityElementView(Context context, Element element, WorkspaceController controller) {
        super(context, element, controller);

        nodeLine = new LinearLayout(context);
        addView(nodeLine);

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
        nodeLine.setLayoutParams(lp);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        view.setImageResource(R.drawable.activity);
    }

    public void add(View v){
        nodeLine.addView(v, this.getChildCount()-2);
    }
}
