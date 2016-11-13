package com.example.a21corp.vinca.Editor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a21corp.vinca.CustomView.BaseElementView;
import com.example.a21corp.vinca.CustomView.ElementView;
import com.example.a21corp.vinca.CustomView.ExpandableElementView;
import com.example.a21corp.vinca.Entitiy.BaseElement;
import com.example.a21corp.vinca.Entitiy.Element;
import com.example.a21corp.vinca.Entitiy.Expandable;
import com.example.a21corp.vinca.Entitiy.Workspace;
import com.example.a21corp.vinca.R;

public class EditorActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnDragListener, View.OnLongClickListener, View.OnTouchListener {

    private View activityView, methodView, pauseView, decisionView, iterateView;
    private View processView, projectView, undoView, redoView, exportView;
    private ImageButton backButton;
    private TextView projectNameBar, saveStatusBar;
    static Workspace ws = null;

    //public static LinearLayout cursor;
    public static LinearLayout canvas;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initiateEditor();
        ws = new Workspace(this);
        gestureDetector = new GestureDetector(this, new EditorGestureDetector());
        paintCanvas();
    }

    private void paintCanvas() {
        for (BaseElement elem : ws.baseElementList) {
            if (elem instanceof Expandable) {
                ((Expandable) elem).view.setOnDragListener(this);
                canvas.addView(((Expandable) elem).view);
            } else if (elem instanceof Element) {
                canvas.addView(((Element) elem).view);
            }
        }
        if (ws.baseElementList.size() == 1 && ws.baseElementList.get(0) instanceof Expandable) {
            canvas = ((Expandable) ws.baseElementList.get(0)).view;
        }
    }

    private void initiateEditor() {
        //Get references
        canvas = (LinearLayout) findViewById(R.id.canvas);
        canvas.setVisibility(View.VISIBLE);
        //cursor = canvas;
        activityView = findViewById(R.id.panel_icon_activity);
        methodView = findViewById(R.id.panel_icon_method);
        pauseView = findViewById(R.id.panel_icon_pause);
        decisionView = findViewById(R.id.panel_icon_decision);
        iterateView = findViewById(R.id.panel_icon_iterate);
        processView = findViewById(R.id.panel_icon_process);
        projectView = findViewById(R.id.panel_icon_project);
        undoView = findViewById(R.id.undo);
        redoView = findViewById(R.id.redo);
        exportView = findViewById(R.id.export);
        backButton = (ImageButton) findViewById(R.id.button_return);
        projectNameBar = (TextView) findViewById(R.id.text_project_name);
        saveStatusBar = (TextView) findViewById(R.id.text_save_status);

        //Listen to VINCA symbols
        activityView.setOnTouchListener(this);
        methodView.setOnTouchListener(this);
        pauseView.setOnTouchListener(this);
        decisionView.setOnTouchListener(this);
        iterateView.setOnTouchListener(this);
        processView.setOnTouchListener(this);
        projectView.setOnTouchListener(this);
        activityView.setOnClickListener(this);
        methodView.setOnClickListener(this);
        pauseView.setOnClickListener(this);
        decisionView.setOnClickListener(this);
        iterateView.setOnClickListener(this);
        processView.setOnClickListener(this);
        projectView.setOnClickListener(this);
        
        //Listen to misc. buttons
        undoView.setOnClickListener(this);
        redoView.setOnClickListener(this);
        exportView.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d("Editor - Debug", "Clicked on " + v.toString());
        //TODO: FIX RESPONSEBILITY!

        int elementType = Integer.valueOf((String) v.getTag());

        addElementToEditor(v, null, elementType);
    }

    @Override
    public boolean onDrag(View view, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d("Editor - Debug", "Drag entered into view: " + view.toString());
                //TODO: Fix this, should highlight border or something
                //TODO: CustomView - isExpandable? React to drag and highlight view
                view.setBackgroundResource(R.color.background_material_light_2);
                if (view instanceof ElementView) {
                    ((ViewGroup) view.getParent())
                            .setBackgroundResource(R.color.background_material_light_2);
                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.d("Editor - Debug", "Drag exited view: " + view.getId() + view.toString());
                //Remove the background - Doesn't matter if no background present
                view.setBackgroundResource(0);
                if (view instanceof ElementView) {
                    ((ViewGroup) view.getParent())
                            .setBackgroundResource(0);
                }
                break;
            case DragEvent.ACTION_DROP:
                view.setBackgroundResource(0);
                if (view instanceof ElementView) {
                    ((ViewGroup) view.getParent())
                            .setBackgroundResource(0);
                }
                View draggdView = (View) event.getLocalState();

                int elementType = Integer.valueOf((String) draggdView.getTag());

                addElementToEditor(draggdView, view, elementType);
        }
        //This listener should always receive drag-events, so always return true!
        return true;
    }

    private void addElementToEditor(View elementView, View parent, int elementType) {
        if (parent == null) {
            parent = canvas;
        }
        if (elementView instanceof ElementView
                || elementView instanceof ExpandableElementView) {
            ((ViewGroup) elementView.getParent()).removeView(elementView);
            ((ViewGroup) parent).addView(elementView);
        } else {
            BaseElement newElement = null;
            BaseElementView newView = null;
            if (elementType== BaseElement.ELEMENT_PROJECT
                    || elementType == BaseElement.ELEMENT_PROCESS
                    || elementType == BaseElement.ELEMENT_ITERATE) {
                newElement = new Expandable();
                newView = new ExpandableElementView(this);
                newView.setType(this, elementType);
                newView.setOnDragListener(this);
            } else if (elementType >= 0 && elementType <= 6) {
                newElement = new Element();
                newView = new ElementView(this);
                newView.setType(this, elementType);
                newView.setOnDragListener(this);
            }
            newElement.view = newView;
            if (parent instanceof ExpandableElementView) {
                ((ViewGroup) parent.findViewWithTag("canvas")).addView(newView);
            }
            else {
                ((ViewGroup) parent.getParent()).addView(newView);
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        startDragAux(view);
        //Symbol should no longer be visible on canvas
        view.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            startDragAux(view);
            return true;
        }
        /**
        else {
            boolean consumed = gestureDetector.onTouchEvent(event);
            Log.d("Editor - Debug", consumed ? "Consumed" : "Not consumed!");
            if (consumed) {
                //TODO: FIX THIS!
                ws.addElement();
            }
            return consumed;
        }
         **/
        return false;
    }

    private void startDragAux(View view) {
        View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(null, dragShadowBuilder, view, 0);
    }
}
