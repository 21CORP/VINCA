package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.a21corp.vinca.Editor.GhostEditorView;
import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by ymuslu on 12-11-2016
 */

public class NodeView extends FrameLayout implements VincaElementView {
    private Node node;
    private ImageView image;
    protected WorkspaceController project;
    public NodeView(Context context, Node node, WorkspaceController histo) {
        super(context);
        this.project = histo;
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

    @Override
    public void setParent(ContainerView container) {
        project.setParent(node, (Container)container.getVincaSymbol());
    }

    @Override
    public void setParent(ElementView element) {
        project.setParent(node, (Element)element.getVincaSymbol());
    }

    @Override
    public void setParent(NodeView n) {
        project.setParent(node, (Node)n.getVincaSymbol());
    }

    @Override
    public void addGhost(GhostEditorView view) {
        VincaElement newView = VincaElement.create(view.getType().type);
        project.setParent(newView, node);
    }

    @Override
    public void remove() {
        project.remove(node);
    }

}
