package com.example.a21corp.vinca.Editor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a21corp.vinca.HistoryManagement.CreateCommand;
import com.example.a21corp.vinca.HistoryManagement.DeleteCommand;
import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.HistoryManagement.MoveCommand;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.element_description;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.vincaviews.ContainerView;
import com.example.a21corp.vinca.vincaviews.ElementView;
import com.example.a21corp.vinca.vincaviews.NodeView;
import com.example.a21corp.vinca.vincaviews.VincaElementView;
import com.example.a21corp.vinca.vincaviews.ExpandableView;

import java.io.File;

public class EditorActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnDragListener, View.OnLongClickListener
        , View.OnTouchListener {

    private VincaViewManager viewManager = null;
    private NodeView methodView;
    private ElementView activityView, pauseView, decisionView;
    private ExpandableView processView, projectView, iterateView;
    //TODO:
    //private View undoView, redoView
    private ImageButton backButton, exportView;
    private ImageButton trashBin;
    private TextView projectNameBar, saveStatusBar;

    public LinearLayout canvas;
    private HorizontalScrollView scrollView;
    public LinearLayout elementPanel;

    private Historian historian;

    //flyttes til andet sted?
    private String dirPath;
    private File projDir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initiateEditor();
        viewManager = new VincaViewManager(this);
        historian = Historian.getInstance();

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
        //Elements
        activityView = new ElementView(this, VincaElement.ELEMENT_ACTIVITY);
        pauseView = new ElementView(this, VincaElement.ELEMENT_PAUSE);
        decisionView = new ElementView(this, VincaElement.ELEMENT_DECISION);
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
        backButton = (ImageButton) findViewById(R.id.button_return);
        trashBin = (ImageButton) findViewById(R.id.trashbin);
        projectNameBar = (TextView) findViewById(R.id.text_project_name);
        saveStatusBar = (TextView) findViewById(R.id.text_save_status);
        projectNameBar.setText(Workspace.getInstance().getTitle());
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
        trashBin.setOnClickListener(this);
        //trashBin.setClickable(false);

       dirPath = getFilesDir().getAbsolutePath() + File.separator + "workspaces";
        projDir = new File(dirPath);
        if (!projDir.exists())
            projDir.mkdirs();

    }

    @Override
    public void onClick(View view) {


        if (view instanceof VincaElementView) {
            if (view.getParent() == elementPanel) {
                CreateCommand cCmd = new CreateCommand((VincaElementView) view, viewManager);
            } else if (view instanceof ContainerView) {
                viewManager.setCursor((ContainerView) view);
            }
        } else if (view == trashBin) {
            //viewManager.deleteElement(viewManager.getCursor());
            throw new IllegalStateException("Testing CrashLytics by clicking TrashBin");
        }
        if(view==exportView){

            ProjectManager.getInstance().saveProject(projDir+"/"
                    + Workspace.getInstance().getTitle() +".ser");

            Log.d("export","serialized");

            for (String s : projDir.list()) {
                System.out.println(s);
            }

        }
            if(view== backButton){
            Log.d("back","clicked");

            ProjectManager.getInstance().loadProject(projDir+"/"
                    + Workspace.getInstance().getTitle() +".ser");

            Intent workspace = new Intent(this, EditorActivity.class);
            finish();
            startActivity(workspace);
        }
    }

    @Override
    public boolean onDrag(View view, DragEvent event) {
        //Log.d("Editor - DragEvent", "drag event recieved: " + DragEvent.class.getDeclaredFields()[event.getAction()]);
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                Log.d("Editor - Debug", "Started dragging " + view.getId());
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                if (view == trashBin) {
                    view.setBackgroundColor(0);
                } else {
                    Log.d("Editor - Debug", "Drag exited view: " + view.getId() + view.toString());
                    viewManager.highlightView(viewManager.getCursor());
                }
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                if (view == trashBin) {
                    view.setBackgroundColor(Color.RED);
                }
                else {
                    Log.d("Editor - Debug", "Drag entered into view: " + view.toString());
                    viewManager.highlightView(view);
                }
                break;
            case DragEvent.ACTION_DROP:
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
                    Log.d("Editor - Debug", "Dropped on view trashbin! - Deleting");
                    view.setBackgroundColor(0);
                    DeleteCommand dCmd = new DeleteCommand(draggedView, viewManager);
                    historian.storeAndExecute(dCmd);

                    break;
                }
                Log.d("Editor - Debug", "Dropped on view: " + ((VincaElementView) view).type + "\n"
                        + ((VincaElementView) view).element.toString());

                if (view instanceof VincaElementView) {
                    if (draggedView instanceof VincaElementView) {
                        MoveCommand mCmd = new MoveCommand(draggedView, viewManager, (VincaElementView) view);
                        historian.storeAndExecute(mCmd);
                    }
                    viewManager.highlightView(viewManager.getCursor());
                    break;
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d("Editor - Debug", "Drop ended!");
                try {
                    draggedView = (VincaElementView) event.getLocalState();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    break;
                }
                draggedView.setVisibility(View.VISIBLE);
                break;
        }
        //This listener should always receive drag-events, so always return true!
        return true;
    }

    @Override
    public boolean onLongClick(View view) {
        //PLACEHOLDER
        //TODO: REPLACE
        if (view instanceof ContainerView) {
            viewManager.toggleOpenExpandableView((ContainerView) view);

            element_description ed = new element_description();
            ed.setElement(((ContainerView) view).element);
            ed.show(getFragmentManager(), "DescriptionWindow");

            return true;
        }
        //PLACEHOLDER END
        if (view instanceof VincaElementView && view.getParent() != elementPanel) {
            //Show menu for title, description etc.
            Log.d("Editor - Debug", "LongClick detected!");
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            Log.d("Editor - Debug", "touch move event recieved");
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
        boolean success = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            success = view.startDragAndDrop(null, dragShadowBuilder, view, 0);
        } else {
            success =  view.startDrag(null, dragShadowBuilder, view, 0);
        }
        if(!success)
        {
            Log.e("Editor", "failed to start dragging");
        }
    }
}
