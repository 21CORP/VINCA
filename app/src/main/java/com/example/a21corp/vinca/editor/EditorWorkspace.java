package com.example.a21corp.vinca.Editor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.elements.BaseElement;
import com.example.a21corp.vinca.elements.Expandable;
import com.example.a21corp.vinca.elements.Holder;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.vincaviews.ElementView;
import com.example.a21corp.vinca.vincaviews.ExpandableElementView;
import com.example.a21corp.vinca.vincaviews.HolderView;
import com.example.a21corp.vinca.vincaviews.NodeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymuslu on 12-11-2016.
 */

public class EditorWorkspace {

    public List<BaseElement> baseElementList = new ArrayList<BaseElement>();
    private Context context;
    public LinearLayout cursor;
    public LinearLayout canvas;

    public EditorWorkspace(Context context) {
        Log.d("EditorWorkspace - Debug", "Creating workspace");
        this.context = context;
        initiateWorkspace();
    }

    public ExpandableElementView initiateWorkspace() {
        Expandable project = new Expandable();
        ExpandableElementView newView = new ExpandableElementView(this.context);
        newView.setType(this.context, BaseElement.ELEMENT_PROJECT);
        project.view = newView;
        cursor = newView;
        canvas = newView;

        addElementToElemList(project);
        addViewToCanvas(newView, cursor);
        return newView;
    }

    //Top-level element has been added
    //No index provided - append to list
    public void addElementToElemList(BaseElement element) {
        baseElementList.add(element);
    }


    //Top-level element added
    public void addElementToElemList(int index, BaseElement element) {
    }


    public void removeViewFromCanvas(View view) {
        if (view == canvas) {
            initiateWorkspace();
        } else if (view == cursor) {
            cursor = canvas;
        } else {
            View parent = (View) view.getParent();
            ((ViewGroup) view.getParent()).removeView(view);
            parent.invalidate();
        }
    }


    public void moveElement(int newIndex, BaseElement element) {
    }

    public View addViewToCanvas(View view, View newParent) {
        if (view == canvas) {
            //User attempted to move the main canvas. Do nothing
            return null;
        }
        if (newParent == null) {
            newParent = cursor;
        }
        if (view instanceof ViewGroup && ((ViewGroup) view).indexOfChild(newParent) >= 0) {
            //Parent is a child of the view user is moving. Do nothing
            return null;
        }
        if (view instanceof  NodeView && newParent instanceof HolderView) {
            ((ImageView) newParent.findViewWithTag("symbol"))
                    .setImageResource(R.drawable.activity_1_method);
            return newParent;
        }
        if (view instanceof ElementView) {
            if (newParent instanceof ExpandableElementView) {
                return setParent(view, (ExpandableElementView) newParent);
            } else if (newParent instanceof ElementView) {
                //Parent is a VINCA element but not an expandable.
                //Cannot be Parent - Put new symbol to the right of it instead
                ViewGroup trueParent = (ViewGroup) newParent.getParent();
                int position = trueParent.indexOfChild(view) + 1;
                return setParent(view, (ElementView) newParent, position);
            }
            //TODO: FIX THIS - Horrible code
        } else if (view.getTag() != null) {
            int elementType = Integer.valueOf((String) view.getTag());
            BaseElement newElement = null;
            ElementView newView = null;
            if (BaseElement.Expendables.contains(elementType)) {
                newElement = new Expandable();
                newView = new ExpandableElementView(this.context);
            }else if (BaseElement.Holder.contains(elementType)) {
                newElement = new Holder();
                newView = new HolderView(this.context);
            } else if (BaseElement.Nodes.contains(elementType)) {
                newElement = new Node();
                newView = new NodeView(this.context);
            }
            if (newElement == null) {
                return null;
            }
            newView.setType(this.context, elementType);
            newElement.view = newView;
            if (newParent instanceof ExpandableElementView) {
                ((ViewGroup) newParent.findViewWithTag("canvas")).addView(newView);
            } else if (newParent.getTag() != null && newParent.getTag() == "canvas") {
                ((ViewGroup) newParent).addView(newView);
            } else {
                ((ViewGroup) newParent.getParent()).addView(newView);
            }
            newParent.invalidate();
            return newView;
        }
        return null;
    }

    private View setParent(View view, ViewGroup newParent) {
        return setParent(view, newParent, -1);
    }


    private View setParent(View view, ViewGroup newParent, int position) {
        View oldParent = (View) view.getParent();
        ((ViewGroup) view.getParent()).removeView(view);
        if (position > 0 && position < newParent.getChildCount()) {
            ((ViewGroup) newParent.findViewWithTag("canvas")).addView(view, position);
        } else {
            ((ViewGroup) newParent.findViewWithTag("canvas")).addView(view);
        }
        oldParent.invalidate();
        newParent.invalidate();
        return view;
    }
}
