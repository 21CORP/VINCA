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
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.vincaviews.ElementView;
import com.example.a21corp.vinca.vincaviews.HolderView;
import com.example.a21corp.vinca.vincaviews.NodeView;
import com.example.a21corp.vinca.vincaviews.VincaElementView;
import com.example.a21corp.vinca.vincaviews.ExpandableView;

public class EditorActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnDragListener, View.OnLongClickListener
        , View.OnTouchListener {

    private VINCAViewManager viewManager = null;

    private NodeView methodView;
    private HolderView activityView, pauseView, decisionView;
    private ExpandableView processView, projectView, iterateView;
    //TODO:
    //private View undoView, redoView
    private ImageButton backButton, trashBin, exportView;
    private TextView projectNameBar, saveStatusBar;

    public LinearLayout canvas;
    private HorizontalScrollView scrollView;
    public LinearLayout elementPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initiateEditor();
        viewManager = new VINCAViewManager(this);
    }

    private void initiateEditor() {
        //Get references
        canvas = (LinearLayout) findViewById(R.id.canvas);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
        elementPanel = (LinearLayout) findViewById(R.id.panel);

        //Expandables
        projectView = new ExpandableView(this, VincaElement.ELEMENT_PROJECT);
        processView = new ExpandableView(this, VincaElement.ELEMENT_PROCESS);
        iterateView = new ExpandableView(this, VincaElement.ELEMENT_ITERATE);
        //Holders
        activityView = new HolderView(this, VincaElement.ELEMENT_ACTIVITY);
        pauseView = new HolderView(this, VincaElement.ELEMENT_PAUSE);
        decisionView = new HolderView(this, VincaElement.ELEMENT_DECISION);
        //Nodes
        methodView = new NodeView(this, VincaElement.ELEMENT_METHOD);

        //Add Vinca Symbols to the panel
        elementPanel.addView(activityView);
        elementPanel.addView(decisionView);
        elementPanel.addView(pauseView);
        elementPanel.addView(methodView);
        elementPanel.addView(iterateView);
        elementPanel.addView(processView);
        elementPanel.addView(projectView);



        //undoView = findViewById(R.id.undo);
        //redoView = findViewById(R.id.redo);
        exportView = (ImageButton) findViewById(R.id.export);
        trashBin = (ImageButton) findViewById(R.id.trashbin);
        backButton = (ImageButton) findViewById(R.id.button_return);
        projectNameBar = (TextView) findViewById(R.id.text_project_name);
        saveStatusBar = (TextView) findViewById(R.id.text_save_status);

        //Listen to VINCA symbols
        projectView.setOnTouchListener(this);
        projectView.setOnClickListener(this);
        processView.setOnTouchListener(this);
        processView.setOnClickListener(this);
        iterateView.setOnTouchListener(this);
        iterateView.setOnClickListener(this);

        activityView.setOnTouchListener(this);
        activityView.setOnClickListener(this);
        pauseView.setOnTouchListener(this);
        pauseView.setOnClickListener(this);
        decisionView.setOnTouchListener(this);
        decisionView.setOnClickListener(this);

        methodView.setOnTouchListener(this);
        methodView.setOnClickListener(this);

        //Listen to misc. buttons
        //undoView.setOnClickListener(this);
        //redoView.setOnClickListener(this);
        exportView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        trashBin.setOnDragListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view instanceof VincaElementView) {
            if (view.getParent() == elementPanel) {
                viewManager.addElement((VincaElementView) view);
            } else if (view instanceof ElementView) {
                viewManager.setCursor((ElementView) view);
            }
        } else if (view == trashBin) {
            viewManager.deleteElement(viewManager.cursor);
        } else if (view == exportView) {
            //TODO: Implement
        } else if (view == backButton) {
            //TODO: Implement
        }
    }

    @Override
    public boolean onDrag(View view, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_EXITED:
                if (view == trashBin) {
                    view.setBackgroundColor(0);
                } else {
                    Log.d("Editor - Debug", "Drag exited view: " + view.getId() + view.toString());
                    //Remove the background - Doesn't matter if no background present
                    view.setBackgroundResource(0);
                    if (view instanceof VincaElementView && view.getParent() != null) {
                        ((ViewGroup) view.getParent().getParent())
                                .setBackgroundResource(0);
                    }
                    viewManager.cursor.setBackgroundResource(R.color.background_material_light_2);
                }
            case DragEvent.ACTION_DRAG_ENTERED:
                if (view == trashBin) {
                    view.setBackgroundColor(Color.RED);
                }
                else {
                    viewManager.cursor.setBackgroundResource(0);
                    Log.d("Editor - Debug", "Drag entered into view: " + view.toString());
                    //TODO: Fix this, should highlight border or something
                    view.setBackgroundResource(R.color.background_material_light_2);
                    if (view instanceof VincaElementView && view.getParent() != null) {
                        ((ViewGroup) view.getParent().getParent())
                                .setBackgroundResource(0);
                    }
                }
                break;
            case DragEvent.ACTION_DROP:
                Log.d("Editor - Debug", "Dropped on view: " + ((VincaElementView) view).type + "\n"
                        + ((VincaElementView) view).element.toString());
                VincaElementView draggedView;
                try {
                    draggedView = (VincaElementView) event.getLocalState();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    break;
                }
                draggedView.setVisibility(View.VISIBLE);

                //User dropped a view on top of itself?
                if (view == draggedView) {
                    break;
                }
                //User dropped a view into trash bin?
                if (view == trashBin) {
                    view.setBackgroundResource(0);
                    viewManager.deleteElement(draggedView);
                    break;
                }

                if (view instanceof VincaElementView) {
                    view.setBackgroundResource(0);
                    if (view instanceof ExpandableView) {
                        ((ViewGroup) view.getParent()).setBackgroundResource(0);
                    }
                    if (draggedView instanceof VincaElementView) {
                        viewManager.moveElement(draggedView, (VincaElementView) view);
                    }
                    viewManager.cursor.setBackgroundResource(R.color.background_material_light_2);
                    break;
                }
                break;
        }
        //This listener should always receive drag-events, so always return true!
        return true;
    }

    @Override
    public boolean onLongClick(View view) {
        if (view instanceof VincaElementView && view.getParent() != elementPanel) {
            //Show menu for title, description etc.
            Log.d("Editor - Debug", "LongClick detected!");
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
        if (view.getParent() != elementPanel) {
            view.setVisibility(View.GONE);
        }
        View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(null, dragShadowBuilder, view, 0);
    }
}
