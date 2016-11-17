package com.example.a21corp.vinca.Editor;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.vincaviews.ElementView;
import com.example.a21corp.vinca.vincaviews.ExpandableElementView;
import com.example.a21corp.vinca.vincaviews.HolderView;

public class EditorActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnDragListener, View.OnLongClickListener, View.OnTouchListener {

    private View activityView, methodView, pauseView, decisionView, iterateView;
    private View processView, projectView;
    //TODO:
    //private View undoView, redoView
    private ImageButton backButton, trashBin, exportView;
    private TextView projectNameBar, saveStatusBar;
    private EditorWorkspace ws = null;

    public LinearLayout canvas;
    private HorizontalScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initiateEditor();
        ws = new EditorWorkspace(this);
        ws.canvas.setOnDragListener(this);
        ws.canvas.setOnClickListener(this);
        ws.canvas.setOnLongClickListener(this);
        canvas.addView(ws.canvas);
        ws.cursor.setBackgroundResource(R.color.background_material_light_2);
    }

    private void initiateEditor() {
        //Get references
        canvas = (LinearLayout) findViewById(R.id.canvas);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
        activityView = findViewById(R.id.panel_icon_activity);
        methodView = findViewById(R.id.panel_icon_method);
        pauseView = findViewById(R.id.panel_icon_pause);
        decisionView = findViewById(R.id.panel_icon_decision);
        iterateView = findViewById(R.id.panel_icon_iterate);
        processView = findViewById(R.id.panel_icon_process);
        projectView = findViewById(R.id.panel_icon_project);
        //undoView = findViewById(R.id.undo);
        //redoView = findViewById(R.id.redo);
        exportView = (ImageButton) findViewById(R.id.export);
        trashBin = (ImageButton) findViewById(R.id.trashbin);
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
        //undoView.setOnClickListener(this);
        //redoView.setOnClickListener(this);
        exportView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        trashBin.setOnDragListener(this);
    }

    @Override
    public void onClick(View view) {
        addElementFromPanel(view);
    }

    private void addElementFromPanel(View view) {
        addElementFromPanel(view, null);
    }

    private void addElementFromPanel(View view, View parent) {
        Log.d("Editor - Debug", "Clicked on " + view.toString());
        if (view == backButton) {
            //TODO: Implement
            Log.d("Editor - Debug", "Tried to return to main menu - function unimplemented");
        } /**else if (view == undoView) {

        } else if (view == redoView) {

        } **/else if (view == exportView) {
            //TODO: Implement
            Log.d("Editor - Debug", "Tried to export - function unimplemented");
        } else if (view instanceof ElementView) {
            ws.cursor.setBackgroundResource(0);
            //cursor = (LinearLayout) view.findViewWithTag("canvas");
            ws.cursor = (ElementView) view;
            ws.cursor.setBackgroundResource(R.color.background_material_light_2);
        } else if (view.getTag() != null){
            addElement(view, parent);
        }
    }

    private void addElement(View view, View parent) {
        final View newView = ws.addViewToCanvas(view, parent);
        if (newView != null) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup newParent = (ViewGroup) newView.getParent();
                    scrollView.smoothScrollTo
                            (newParent.getChildAt(newParent.getChildCount()-1).getRight(), 0);
                }
            });
            newView.setOnDragListener(this);
            newView.setOnTouchListener(this);
            newView.setOnClickListener(this);
            newView.setOnLongClickListener(this);
        }
    }

    @Override
    public boolean onDrag(View view, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_ENTERED:
                if (view == trashBin) {
                    view.setBackgroundColor(Color.RED);
                }
                else {
                    ws.cursor.setBackgroundResource(0);
                    Log.d("Editor - Debug", "Drag entered into view: " + view.toString());
                    //TODO: Fix this, should highlight border or something
                    view.setBackgroundResource(R.color.background_material_light_2);
                    if (view instanceof ElementView && !(view instanceof ExpandableElementView)) {
                        ((ViewGroup) view.getParent().getParent())
                                .setBackgroundResource(0);
                    }
                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                if (view == trashBin) {
                    view.setBackgroundColor(0);
                } else {
                    Log.d("Editor - Debug", "Drag exited view: " + view.getId() + view.toString());
                    //Remove the background - Doesn't matter if no background present
                    view.setBackgroundResource(0);
                    if (view instanceof ElementView && !(view instanceof ExpandableElementView)) {
                        ((ViewGroup) view.getParent().getParent())
                                .setBackgroundResource(0);
                    }
                    ws.cursor.setBackgroundResource(R.color.background_material_light_2);
                }
                break;
            case DragEvent.ACTION_DROP:
                View draggedView = (View) event.getLocalState();
                draggedView.setVisibility(View.VISIBLE);

                //User dropped a view on top of itself?
                if (view == draggedView) {
                    break;
                }
                //User dropped a view into trash bin?
                if (view == trashBin) {
                    view.setBackgroundResource(0);
                    ws.removeViewFromCanvas(draggedView);
                    break;
                }

                if (view instanceof ElementView) {
                    view.setBackgroundResource(0);
                    if (view instanceof ExpandableElementView) {
                        ((ViewGroup) view.getParent()).setBackgroundResource(0);
                    }
                    if (draggedView instanceof ElementView) {
                        addElement(draggedView, view);
                    }
                    else {
                        addElementFromPanel(draggedView, view);
                    }
                    ws.cursor.setBackgroundResource(R.color.background_material_light_2);
                    break;
                }
                break;
        }
        //This listener should always receive drag-events, so always return true!
        return true;
    }

    @Override
    public boolean onLongClick(View view) {
        if (view instanceof ElementView) {
            //Show menu for title, description etc.
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            startDragAux(view);
            return true;
        }
        //Did not consume event
        return false;
    }

    private void startDragAux(View view) {
        if (view instanceof ElementView) {
            view.setVisibility(View.GONE);
        }
        View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(null, dragShadowBuilder, view, 0);
    }
}
