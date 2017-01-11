package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a21corp.vinca.Editor.GhostEditorView;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 12-11-2016.
 */

public interface VincaElementView{
    VincaElement getVincaSymbol();
    void moveHere(ContainerView container);
    void moveHere(ElementView element);
    void moveHere(NodeView node);
    void add(GhostEditorView view);
    void remove();

}
