package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a21corp.vinca.Editor.GhostEditorView;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 12-11-2016.
 */

public interface VincaElementView extends View.OnDragListener {
    VincaElement getVincaElement();
    View getView();
    void setParent(ContainerView container);
    void setParent(ElementView element);
    void setParent(NodeView node);
    void addGhost(GhostEditorView view);
    void remove();
    void highlight();

    @Override
    boolean onDrag(View v, DragEvent event);
}
