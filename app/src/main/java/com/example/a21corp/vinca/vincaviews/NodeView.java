package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
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
    private ImageView view;
    protected WorkspaceController project;
    public NodeView(Context context, Node node, WorkspaceController histo) {
        super(context);
        this.project = histo;
        this.node = node;
        //inflate(context, R.layout.node_view, this);
        //view = (ImageView)ImageView.inflate(getContext(), R.layout.vinca_icon_layout, null);
        view = (ImageView) ImageView.inflate(getContext(), R.layout.vinca_icon_layout, null);
        onFinishInflate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        view.setImageResource(R.drawable.method);
        Log.d("NodeView", "finished inflating");
    }
    @Override
    public VincaElement getVincaElement() {
        return node;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void setParent(ContainerView container) {
        project.setParent(node, (Container)container.getVincaElement());
    }

    @Override
    public void setParent(ElementView element) {
        project.setParent(node, (Element)element.getVincaElement());
    }

    @Override
    public void setParent(NodeView n) {
        project.setParent(node, n.getVincaElement().parent);
    }

    @Override
    public void addGhost(GhostEditorView view) {

    }

    @Override
    public void remove() {
        project.remove(node);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        return false;
    }

}
