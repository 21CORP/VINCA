package com.example.a21corp.vinca.Editor;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a21corp.vinca.AutoSaver;
import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.SaveAsDialog;
import com.example.a21corp.vinca.element_description;
import com.example.a21corp.vinca.vincaviews.ActivityElementView;
import com.example.a21corp.vinca.vincaviews.ContainerView;
import com.example.a21corp.vinca.vincaviews.DecisionElementView;
import com.example.a21corp.vinca.vincaviews.ElementView;
import com.example.a21corp.vinca.vincaviews.IterateContainerView;
import com.example.a21corp.vinca.vincaviews.NodeView;
import com.example.a21corp.vinca.vincaviews.PauseElementView;
import com.example.a21corp.vinca.vincaviews.ProcessContainerView;
import com.example.a21corp.vinca.vincaviews.ProjectContainerView;
import com.example.a21corp.vinca.vincaviews.VincaElementView;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class EditorActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnDragListener, View.OnLongClickListener
        , View.OnTouchListener, TextView.OnEditorActionListener {

    private VincaViewManager viewManager = null;
    private NodeView methodView;
    private ActivityElementView activityView;
    private ElementView  pauseView, decisionView;
    private ContainerView processView, projectView, iterateView;
    //TODO:
    //private View undoView, redoView
    private ImageButton backButton, exportView, saveButton;
    private ImageButton trashBin;
    private EditText projectNameBar;
    private TextView saveStatusBar;
    public LinearLayout canvas;
    private HorizontalScrollView scrollView;
    public LinearLayout elementPanel;

    private Historian historian;

    //flyttes til andet sted? JA
    private String dirPath;
    private File projDir;

    //test
    Button undoButton;
    Button redoButton;
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
        trashBin.setOnDragListener(new TrashBin(this));
        historian = Historian.getInstance();

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
        projectView = new ProjectContainerView(this, null);
        processView = new ProcessContainerView(this, null);
        iterateView = new IterateContainerView(this, null);
        //Elements
        pauseView = new PauseElementView(this, null);
        decisionView = new DecisionElementView(this, null);
        //Nodes
        methodView = new NodeView(this, null);
        activityView = new ActivityElementView(this, null);
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
        backButton = (ImageButton) findViewById(R.id.button_return);
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
        backButton.setOnClickListener(this);
        projectNameBar.setOnEditorActionListener(this);

        trashBin.setOnClickListener(this);
        //trashBin.setClickable(false);

        undoButton = (Button) findViewById(R.id.button);
        redoButton = (Button) findViewById(R.id.button2);
        undoButton.setOnClickListener(this);
        redoButton.setOnClickListener(this);

        //TODO flyt til bedre sted?
        if(autoSaver != null){
            autoSaver.timer.cancel();
        }
        autoSaver = new AutoSaver(this);
        autoSaver.timer.start();
        //TODO
    }

    @Override
    public void onClick(View view) {


        if (view instanceof VincaElementView) {
            if (view.getParent() == elementPanel) {
                viewManager.addElement((VincaElementView) view);
            }
        }
        if(view==saveButton){
            SaveAsDialog savepop = new SaveAsDialog();
            savepop.setCurrentWorkspace(viewManager.workspaceController.workspace);
            savepop.show(getFragmentManager(),"save as");

            Log.d("export","serialized");

            for (String s : projDir.list()) {
                System.out.println(s);
            }

        }
        if (view == exportView) {
            ExportDialog exportDialog = new ExportDialog();
            exportDialog.show(getFragmentManager(), "Export as");
            //ProjectManager.saveProject(viewManager.workspaceController.workspace, dirPath);
        }
            if(view== backButton){
            Log.d("back","clicked");

            Workspace workspace;
            try {
                workspace = ProjectManager.loadProject(dirPath + "/"
                        + viewManager.getWorkspaceTitle() + ".ser");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            viewManager.setWorkspace(workspace);
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

            element_description ed = new element_description();
            ed.setElement(((ContainerView) view).element);
            ed.show(getFragmentManager(), "DescriptionWindow");

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
}
