package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.Editor.GhostEditorView;
import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 12-11-2016
 */

public class ElementView extends FrameLayout implements VincaElementView{

    public Element vincaElement;
    protected ImageView symbol;
    protected Historian project;
    public ElementView(Context context, Element element) {
       super(context);
       this.vincaElement = element;
       //inflate(context, R.layout.element_view, this);
       symbol = (ImageView)ImageView.inflate(getContext(), R.layout.vinca_icon_layout, null);
       onFinishInflate();
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        symbol.setImageResource(R.drawable.decision);
        Log.d("ElementView", "finished inflating");
    }

    @Override
    public VincaElement getVincaSymbol() {
        return vincaElement;
    }

    @Override
    public void moveHere(ContainerView container) {
        project.move(vincaElement, (Container) container.getVincaSymbol());
    }

    @Override
    public void moveHere(ElementView element) {
        project.move(vincaElement, (Element)element.getVincaSymbol());
    }

    @Override
    public void moveHere(NodeView node) {
        project.move(vincaElement, (Node)node.getVincaSymbol());
    }

    @Override
    public void add(GhostEditorView view) {
        VincaElement newView = new VincaElement(view.getType());
        project.move(newView, vincaElement);
    }

    @Override
    public void remove() {
        project.remove(vincaElement);
    }
}
