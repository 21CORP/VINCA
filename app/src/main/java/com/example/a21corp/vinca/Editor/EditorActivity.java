package com.example.a21corp.vinca.Editor;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.vincaviews.ContainerView;
import com.example.a21corp.vinca.vincaviews.ElementView;
import com.example.a21corp.vinca.vincaviews.NodeView;
import com.example.a21corp.vinca.vincaviews.VincaElementView;
import com.example.a21corp.vinca.vincaviews.VincaViewFabricator;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.RunnableFuture;

public class EditorActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener, View.OnDragListener
        , View.OnTouchListener, TextView.OnEditorActionListener, WorkspaceObserver {
    private WorkspaceController controller;
    private GhostEditorView methodView;
    private GhostEditorView activityView;
    private GhostEditorView  pauseView, decisionView;
    private GhostEditorView processView, projectView, iterateView;
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
        ProjectManager.saveProject(controller.workspace, dirPath);
        outState.putString("title", controller.workspace.getTitle());
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
        trashBin.setOnDragListener(new TrashBin(this));
        historian = Historian.getInstance();

    }

    @Override
    protected void onStart(){
        if(autoSaver != null){
            autoSaver.timer.cancel();
        }
        autoSaver = new AutoSaver(this, controller.workspace, dirPath);
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
        controller = new WorkspaceController(workspace);
        controller.observerList.add(this);
        updateCanvas();
        projectNameBar.setText(title);
    }

    private void initiateEditor() {
        //Get references
        canvas = (LinearLayout) findViewById(R.id.canvas);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
        elementPanel = (LinearLayout) findViewById(R.id.panel);

        //Containers
        projectView = new GhostEditorView(this, (Container)VincaElement.create(VincaElement.ELEMENT_PROJECT));
        processView = new GhostEditorView(this, (Container)VincaElement.create(VincaElement.ELEMENT_PROCESS));
        iterateView = new GhostEditorView(this, (Container)VincaElement.create(VincaElement.ELEMENT_ITERATE));
        //Elements
        pauseView = new GhostEditorView(this, VincaElement.create(VincaElement.ELEMENT_PAUSE));
        decisionView = new GhostEditorView(this, VincaElement.create(VincaElement.ELEMENT_DECISION));
        //Nodes
        methodView = new GhostEditorView(this, VincaElement.create(VincaElement.ELEMENT_METHOD));
        activityView = new GhostEditorView(this, VincaElement.create(VincaElement.ELEMENT_ACTIVITY));
        //Add Vinca Symbols to the panel-.-

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


        if (view instanceof GhostEditorView) {
            controller.addVincaElement(((GhostEditorView) view).getVincaElement());
        }
        if(view==saveButton){
            SaveAsDialog savepop = new SaveAsDialog();
            savepop.setCurrentWorkspace(controller.workspace);
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
    public boolean onLongClick(View view) {
        //PLACEHOLDER
        //TODO: REPLACE
        if (view instanceof ContainerView) {
            controller.toggleOpenContainer(((ContainerView) view).getVincaElement());

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
        view.setOnDragListener(this);
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
        if (title.isEmpty() || title.equals(controller.getWorkspaceTitle())) {
            return false;
        } else {
            controller.renameWorkspace(title, dirPath);
            return true;
        }
    }

    @Override
    public void updateCanvas() {
        Container vincaRoot = controller.workspace.projects.get(0);
        VincaViewFabricator fabricator = new VincaViewFabricator(this, controller);
        View root = fabricator.getVincaView(vincaRoot);
        this.canvas.removeAllViews();
        this.canvas.addView(root);
        this.canvas.invalidate();
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        if (event.getAction() == DragEvent.ACTION_DRAG_ENDED) {
            View draggedView = (View) event.getLocalState();
            draggedView.setVisibility(View.VISIBLE);
            if (! (draggedView instanceof GhostEditorView)) {
                try {
                    draggedView.setOnDragListener((VincaElementView) draggedView);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }

        }
        return true;
    }
    @Override
    protected void onStop(){
        ProjectManager.saveProject(controller.workspace, dirPath);
        autoSaver.timer.cancel();
        super.onStop();
    }

    public void canvasToJPG() {
        if (controller == null) {
            return;
        }
        Element cursor = controller.workspace.getCursor();
        controller.setCursor(null);

        Container vincaRoot = controller.workspace.projects.get(0);
        VincaViewFabricator fabricator = new VincaViewFabricator(this, controller);
        ViewGroup viewToExport = (ViewGroup) fabricator.getVincaView(vincaRoot);

        controller.setCursor(cursor);

        viewToExport.setBackgroundColor(Color.WHITE);
        viewToExport.getChildAt(0).setBackgroundColor(Color.WHITE);
        ExportDialog exportDialog = new ExportDialog();
        exportDialog.setExportTarget(this, viewToExport, controller.getWorkspaceTitle());
        exportDialog.show(getFragmentManager(), "Export as");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);

    }


}
