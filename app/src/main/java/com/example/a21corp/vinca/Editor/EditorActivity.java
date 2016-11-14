package com.example.a21corp.vinca.editor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a21corp.vinca.elements.Holder;
import com.example.a21corp.vinca.elements.BaseElement;
import com.example.a21corp.vinca.elements.Expandable;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.vincaviews.ElementView;
import com.example.a21corp.vinca.vincaviews.ExpandableElementView;
import com.example.a21corp.vinca.vincaviews.HolderView;
import com.example.a21corp.vinca.vincaviews.NodeView;

public class EditorActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnDragListener, View.OnLongClickListener, View.OnTouchListener {

    private View activityView, methodView, pauseView, decisionView, iterateView;
    private View processView, projectView;
    //private View undoView, redoView
    private View exportView;
    private ImageButton backButton, trashbin;
    private TextView projectNameBar, saveStatusBar;
    static EditorWorkspace ws = null;

    //public static LinearLayout cursor;
    public static LinearLayout cursor;
    private HorizontalScrollView scrollView;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initiateEditor();
        ws = new EditorWorkspace(this);
        gestureDetector = new GestureDetector(this, new EditorGestureDetector());
        paintCanvas();
        cursor.setBackgroundResource(R.color.background_material_light_2);
    }

    private void paintCanvas() {
        for (BaseElement elem : ws.baseElementList) {
            if (elem instanceof Expandable) {
                ((Expandable) elem).view.setOnDragListener(this);
                ((Expandable) elem).view.setOnClickListener(this);
                cursor.addView(((Expandable) elem).view);
            } else if (elem instanceof Holder) {
                cursor.addView(((Holder) elem).view);
            }
        }
        if (ws.baseElementList.size() == 1 && ws.baseElementList.get(0) instanceof Expandable) {
            cursor = ((Expandable) ws.baseElementList.get(0)).view;
        }
    }

    private void initiateEditor() {
        //Get references
        cursor = (LinearLayout) findViewById(R.id.canvas);
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
        //undoView.setOnClickListener(this);
        //redoView.setOnClickListener(this);
        exportView.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
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
            cursor.setBackgroundResource(0);
            //cursor = (LinearLayout) view.findViewWithTag("canvas");
            cursor = (ElementView) view;
            cursor.setBackgroundResource(R.color.background_material_light_2);
        } else if (view.getTag() != null){
            addElement(view);
        }
    }

    private void addElement(View view) {
        int elementType = Integer.valueOf((String) view.getTag());
        View newView = ws.addElementToWorkspace(view, null, elementType, this);
        if (newView != null) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo
                            (scrollView.getChildAt(scrollView.getChildCount()-1).getRight(), 0);
                }
            });
        }
    }

    @Override
    public boolean onDrag(View view, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_ENTERED:
                cursor.setBackgroundResource(0);
                Log.d("Editor - Debug", "Drag entered into view: " + view.toString());
                //TODO: Fix this, should highlight border or something
                //TODO: CustomView - isExpandable? React to drag and highlight view
                view.setBackgroundResource(R.color.background_material_light_2);
                if (view instanceof HolderView) {
                    ((ViewGroup) view.getParent().getParent())
                            .setBackgroundResource(0);
                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.d("Editor - Debug", "Drag exited view: " + view.getId() + view.toString());
                //Remove the background - Doesn't matter if no background present
                view.setBackgroundResource(0);
                if (view instanceof HolderView) {
                    ((ViewGroup) view.getParent().getParent())
                            .setBackgroundResource(0);
                }
                cursor.setBackgroundResource(R.color.background_material_light_2);
                break;
            case DragEvent.ACTION_DROP:
                View draggedView = (View) event.getLocalState();
                draggedView.setVisibility(View.VISIBLE);
                if (view == draggedView) {
                    break;
                }
                view.setBackgroundResource(0);
                if (view instanceof ExpandableElementView) {
                    ((ViewGroup) view.getParent()).setBackgroundResource(0);
                }
                if (draggedView instanceof NodeView && view instanceof HolderView) {
                    //Add VINCA Method
                }
                if (draggedView instanceof ElementView) {
                    if (view instanceof ExpandableElementView) {
                        setParent(draggedView, (ExpandableElementView) view);
                    } else if (view instanceof ElementView) {
                        int positionOfElement = ((ViewGroup) view.getParent()).indexOfChild(view);
                        setParent(draggedView, (ViewGroup) view.getParent(), positionOfElement);
                    }
                } else if (draggedView.getTag() != null) {
                    int elementType = Integer.valueOf((String) draggedView.getTag());
                    View newView = ws.addElementToWorkspace(draggedView, view, elementType, this);
                    if (newView != null) {
                        scrollView.smoothScrollTo(scrollView.getBottom() + 50, 0);
                    }
                }
                cursor.setBackgroundResource(R.color.background_material_light_2);
                break;
        }
        //This listener should always receive drag-events, so always return true!
        return true;
    }

    private void setParent(View view, ViewGroup parent, int position) {
        ((ViewGroup) view.getParent()).removeView(view);
        //Find canvas in parent
        ViewGroup canvas = (ViewGroup) parent.findViewWithTag("canvas");
        canvas.addView(view, position);
    }

    private void setParent(View view, ViewGroup parent) {
        ((ViewGroup) view.getParent()).removeView(view);
        //Find canvas in parent
        ViewGroup canvas = (ViewGroup) parent.findViewWithTag("canvas");
        canvas.addView(view);
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
