package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.a21corp.vinca.Editor.EditorActivity;
import com.example.a21corp.vinca.Editor.WorkspaceController;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.elements.VincaElement;

/**
 * Created by root on 1/11/17.
 */

public class VincaViewFabricator {
    private Context context;
    private EditorActivity editor;
    private WorkspaceController project;
    public VincaViewFabricator(EditorActivity editor, WorkspaceController proj)
    {
        this.editor = editor;
        project = proj;
        context = editor.getBaseContext();
    }
    public View getVincaView(VincaElement element)
    {
        View view = null;
        if(VincaElement.Expendables.contains(element.type))
        {
            view = getVincaContainerView((Container)element);
        }
        else if(VincaElement.Elements.contains(element.type)){
            view = getVincaElementView((Element)element);
        }
        else if(VincaElement.Nodes.contains(element.type)){
            view = getVincaNodeView((Node)element);
        }
        view.setOnTouchListener(editor);
        return view;
    }

    private View getVincaNodeView(Node element) {
        switch(element.type)
        {
            case VincaElement.ELEMENT_METHOD:
            {
                NodeView newView = new NodeView(context, element, project);
                View viewToReturn = newView.getView();
                return viewToReturn;
            }
        }
        try {
            throw new ClassNotFoundException("Unable to determine type of element");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public View getVincaElementView(Element element){
        ElementView newView = null;
        switch (element.type)
        {
            case VincaElement.ELEMENT_ACTIVITY:
            {
                newView = new ActivityElementView(context, element, project);
                break;
            }
            case VincaElement.ELEMENT_DECISION:
            {
                newView = new DecisionElementView(context, element, project);
                break;
            }
            case VincaElement.ELEMENT_PAUSE:
            {
                newView = new PauseElementView(context, element, project);
                break;
            }

        }
        try {
            if (newView == null) {
                throw new ClassNotFoundException("Unable to determine type of element");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        View viewToReturn = newView.getView();
        return viewToReturn;
    }



    public ContainerView getVincaContainerView(Container element)
    {
        ContainerView newView = null;
        switch(element.type)
        {
            case VincaElement.ELEMENT_PROJECT:
            {
                newView = new ProjectContainerView(context, element, project);
                break;
            }
            case VincaElement.ELEMENT_PROCESS:
            {
                newView = new ProcessContainerView(context, element, project);
                break;
            }
            case VincaElement.ELEMENT_ITERATE:
            {
                newView = new IterateContainerView(context, element, project);
                break;
            }
        }
        try {
            if (newView == null) {
                throw new ClassNotFoundException("Unable to determine type of element");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < element.containerList.size(); i++) {
            View child = getVincaView(element.containerList.get(i));
            child.setOnTouchListener(editor);
            newView.add(child);
        }
        ContainerView viewToReturn = newView.getView();
        return viewToReturn;
    }
}
