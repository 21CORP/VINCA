package com.example.a21corp.vinca.Editor;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a21corp.vinca.AutoSaver;
import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.ImageExporter;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.SaveAsDialog;
import com.example.a21corp.vinca.element_description;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.vincaviews.ContainerView;
import com.example.a21corp.vinca.vincaviews.ElementView;
import com.example.a21corp.vinca.vincaviews.NodeView;
import com.example.a21corp.vinca.vincaviews.VincaElementView;
import com.example.a21corp.vinca.vincaviews.ExpandableView;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class EditorActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnDragListener, View.OnLongClickListener
        , View.OnTouchListener, TextView.OnEditorActionListener {

    private static final int WRITE_EXTERNAL_STORAGE_STATE = 255;
    private ImageExporter imgExp = new ImageExporter();
    private VincaViewManager viewManager = null;
    private NodeView methodView;
    private ElementView activityView, pauseView, decisionView;
    private ExpandableView processView, projectView, iterateView;
    //TODO:
    //private View undoView, redoView
    private ImageButton exportView, saveButton, undoButton, redoButton;
    private ImageButton trashBin;
    private EditText projectNameBar;
    private TextView saveStatusBar;
    public LinearLayout canvas;
    public HorizontalScrollView scrollView;
    public LinearLayout elementPanel;

    private Historian historian;

    //flyttes til andet sted? JA
    private String dirPath;
    private File projDir;

    //test

    //test end
    private AutoSaver autoSaver;
    private Date timeLastSaved;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        ProjectManager.saveProject(viewManager.workspaceController.workspace, dirPath);
        outState.putString("title", viewManager.getWorkspaceTitle());
        Log.d("Editor - onSaveInstance", outState.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        dirPath = getFilesDir().getAbsolutePath() + File.separator + "workspaces";
        projDir = new File(dirPath);
        if (!projDir.exists()) {
            projDir.mkdirs();
        }

        initiateEditor();
        initiateWorkspace(savedInstanceState);
        trashBin.setOnDragListener(new TrashBin(viewManager));
        historian = Historian.getInstance();

    }

    @Override
    protected void onStart(){
        if(autoSaver != null){
            autoSaver.timer.cancel();
        }
        autoSaver = new AutoSaver(this, viewManager.workspaceController.workspace, dirPath);
        super.onStart();
    }

    private void initiateWorkspace(Bundle savedInstanceState) {
        String title;
        if (savedInstanceState == null) {
            title = getIntent().getExtras().getString("title");
        } else {
            Log.d("Editor - onCreate", savedInstanceState.toString());
            title = savedInstanceState.getString("title");
        }
        Workspace workspace;
        try {
            workspace = ProjectManager.loadProject(dirPath + "/" + title + ".ser");
        } catch (Exception e) {
            e.printStackTrace();
            workspace = ProjectManager.createProject(title);
        }
        projectNameBar.setText(title);
        viewManager = new VincaViewManager(this, workspace);

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
        saveButton = (ImageButton) findViewById(R.id.saveas) ;
        exportView = (ImageButton) findViewById(R.id.export);

       // backButton = (ImageButton) findViewById(R.id.button_return);
        trashBin = (ImageButton) findViewById(R.id.trashbin);
        projectNameBar = (EditText) findViewById(R.id.text_project_name);
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
        saveButton.setOnClickListener(this);
        exportView.setOnClickListener(this);
        //backButton.setOnClickListener(this);
        projectNameBar.setOnEditorActionListener(this);

        trashBin.setOnClickListener(this);
        //trashBin.setClickable(false);

        undoButton = (ImageButton) findViewById(R.id.buttonUndo);
        redoButton = (ImageButton) findViewById(R.id.buttonRedo);
        undoButton.setOnClickListener(this);
        redoButton.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {


        if (view instanceof VincaElementView) {
            if (view.getParent() == elementPanel) {
                viewManager.addElement((VincaElementView) view);
            } else if (view instanceof ContainerView) {
                viewManager.setCursor((ContainerView) view);
                //TODO minimenu open
                ((ContainerView) view).quickTitle.setText(((ContainerView) view).element.title);
            }
        }
        if(view==saveButton){
           ;
            SaveAsDialog savepop = new SaveAsDialog();
            savepop.setCurrentWorkspace(viewManager.workspaceController.workspace);
            savepop.show(getFragmentManager(),"save as");




            Log.d("export","serialized");

            for (String s : projDir.list()) {
                System.out.println(s);
            }

        }
        if (view == exportView) {
            canvasToJPG();
            //ProjectManager.saveProject(viewManager.workspaceController.workspace, dirPath);


        }
        //test
        if(view == undoButton){
            historian.undo();
        }
        if(view == redoButton){
            historian.redo();
        }
    }

    @Override
    public boolean onDrag(View view, DragEvent event) {
        View draggedView = (View) event.getLocalState();
        if (! (draggedView instanceof VincaElementView)) {
            //Not dragging a VincaElementView, not interested in future drag-events
            return false;
        }
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_EXITED:
                    viewManager.highlightView(viewManager.getCursor());
                break;

            case DragEvent.ACTION_DRAG_ENTERED:
                    viewManager.highlightView(view);
                break;

            case DragEvent.ACTION_DROP:
                draggedView.setVisibility(View.VISIBLE);
                if (view instanceof VincaElementView) {
                    viewManager.moveElement((VincaElementView) draggedView, (VincaElementView) view);
                    viewManager.highlightView(viewManager.getCursor());
                    break;
                }
                break;

            case DragEvent.ACTION_DRAG_ENDED:
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

            return true;
        }
        //PLACEHOLDER END
        if (view instanceof VincaElementView && view.getParent() != elementPanel) {
            //Show menu for title, description etc.
            Log.d("WorkspaceController", "LongClick detected!");
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
        boolean success = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            success = view.startDragAndDrop(null, dragShadowBuilder, view, 0);
        } else {
            success =  view.startDrag(null, dragShadowBuilder, view, 0);
        }
        if(!success)
        {
            Log.e("WorkspaceController", "failed to start dragging");
        }
    }

    public void setSaveStatusBar(Boolean status){
        if(status){
            saveStatusBar.setText("Saving...");
            timeLastSaved = Calendar.getInstance().getTime();
        }
        else{
            if (timeLastSaved != null) {
                saveStatusBar.setText("Saved on " + timeLastSaved);
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String title = projectNameBar.getText().toString();
        if (title.isEmpty() || title.equals(viewManager.getWorkspaceTitle())) {
            return false;
        } else {
            viewManager.renameWorkspace(title, dirPath);
            return true;
        }
    }
    @Override
    protected void onStop(){
        ProjectManager.saveProject(viewManager.workspaceController.workspace, dirPath);
        autoSaver.timer.cancel();
        super.onStop();
    }

    public void canvasToJPG() {
        if (viewManager == null) {
            return;
        }
        View viewToExport =
                viewManager.makeViewFromClass
                        (viewManager.workspaceController.workspace.projects.get(0));
        ExportDialog exportDialog = new ExportDialog();
        exportDialog.setExportTarget(this, viewToExport, viewManager.getWorkspaceTitle());
        exportDialog.show(getFragmentManager(), "Export as");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);

    }


}
