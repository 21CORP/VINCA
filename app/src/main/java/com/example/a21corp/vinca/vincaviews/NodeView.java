package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.a21corp.vinca.Editor.GhostEditorView;
import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.elements.VincaActivity;

/**
 * Created by ymuslu on 12-11-2016
 */

public class NodeView extends FrameLayout implements VincaElementView {
    private Node vincaElement;
    private ImageView view;
    protected WorkspaceController project;
    public NodeView(Context context, Node vincaElement, WorkspaceController histo) {
        super(context);
        this.project = histo;
        this.vincaElement = vincaElement;
        //inflate(context, R.layout.node_view, this);
        //view = (ImageView)ImageView.inflate(getContext(), R.layout.vinca_icon_layout, null);
        view = (ImageView) ImageView.inflate(getContext(), R.layout.vinca_icon_layout, null);
        onFinishInflate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        view.setImageResource(R.drawable.method);
    }
    @Override
    public Node getVincaElement() {
        return vincaElement;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void setParent(ContainerView container) {
        //project.setParent(node, (Container)container.getVincaElement());
    }

    @Override
    public void setParent(ElementView element) {
        //project.setParent(node, (Element)element.getVincaElement());
    }

    @Override
    public void setParent(NodeView view) {
        Node node = view.getVincaElement();
        VincaActivity vincaActivity = node.parent;
        project.setParent(vincaElement, vincaActivity, vincaActivity.nodes.size());
    }

    public void setParent(ActivityElementView view) {
        VincaActivity vincaActivity = view.getVincaElement();
        project.setParent(vincaElement, vincaActivity, vincaActivity.nodes.size());
    }

    @Override
    public void addGhost(GhostEditorView view) {

    }

    @Override
    public void remove() {
        project.remove(vincaElement);
    }

    @Override
    public void highlight() {

    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        return false;
    }

}
