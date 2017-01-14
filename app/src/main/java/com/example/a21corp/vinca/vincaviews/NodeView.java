package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.a21corp.vinca.Editor.GhostEditorView;
import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.HistoryManagement.DeleteCommand;
import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.HistoryManagement.MoveCommand;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.elements.VincaActivity;

/**
 * Created by ymuslu on 12-11-2016
 */

public class NodeView extends FrameLayout implements VincaElementView, View.OnClickListener{
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
    public void setParent(ContainerView container) { //Not allowed by grammar
        //project.setParent(node, (Container)container.getVincaElement());
    }

    @Override
    public void setParent(ElementView element) { //Not allowed by grammar
        //project.setParent(node, (Element)element.getVincaElement());
    }

    @Override
    public void setParent(NodeView view) {
        Node node = view.getVincaElement();
        VincaActivity vincaActivity = node.parent;
        //project.setParent(vincaElement, vincaActivity, vincaActivity.nodes.size());
        Historian.getInstance().storeAndExecute(new MoveCommand(vincaElement, vincaActivity, vincaElement.parent, vincaActivity.nodes.size(), vincaElement.parent.nodes.indexOf(vincaElement), project));
    }

    public void setParent(ActivityElementView view) {
        VincaActivity vincaActivity = view.getVincaElement();
        //project.setParent(vincaElement, vincaActivity, vincaActivity.nodes.size());
        Historian.getInstance().storeAndExecute(new MoveCommand(vincaElement, vincaActivity, vincaElement.parent, vincaActivity.nodes.size(), vincaElement.parent.nodes.indexOf(vincaElement), project));
    }

    @Override
    public void addGhost(GhostEditorView view) { //Not allowed by grammar

    }

    @Override
    public void remove() {
        //project.remove(vincaElement);
        Historian.getInstance().storeAndExecute(new DeleteCommand(vincaElement, vincaElement.parent, vincaElement.parent.nodes.indexOf(vincaElement), project));
    }

    @Override
    public void highlight() {

    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {
        System.out.println("Animate!");
        Animation pressAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shakeanim);
        v.startAnimation(pressAnimation);
    }

}
