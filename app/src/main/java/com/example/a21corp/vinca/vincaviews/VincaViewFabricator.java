package com.example.a21corp.vinca.vincaviews;

import android.content.Context;
import android.view.View;

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
    private WorkspaceController project;
    public VincaViewFabricator(Context c, WorkspaceController proj)
    {
        project = proj;
        context = c;
    }
    public View getVincaView(VincaElement element)
    {
        if(VincaElement.Expendables.contains(element.type))
        {
            return getVincaContainerView((Container)element);
        }
        else if(VincaElement.Elements.contains(element.type)){
            return getVincaElementView((Element)element);
        }
        else if(VincaElement.Nodes.contains(element.type)){
            return getVincaNodeView((Node)element);
        }
        return null;
    }

    private NodeView getVincaNodeView(Node element) {
        switch(element.type)
        {
            case VincaElement.ELEMENT_METHOD:
            {
                return new NodeView(context, element, project);
            }
        }
        return null;
    }

    public ElementView getVincaElementView(Element element){
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
        return newView;
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
        for (int i = 0; i < element.containerList.size(); i++) {
            View child = getVincaView(element.containerList.get(i));
            newView.add(child);
        }
        return newView;
    }
}
