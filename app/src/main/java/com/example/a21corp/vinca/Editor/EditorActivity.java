package com.example.a21corp.vinca.Editor;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a21corp.vinca.AutoSaver;
import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.SaveAsDialog;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.vincaviews.ContainerView;
import com.example.a21corp.vinca.vincaviews.VincaElementView;
import com.example.a21corp.vinca.vincaviews.VincaViewFabricator;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class EditorActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener, View.OnDragListener
        , View.OnTouchListener, TextView.OnEditorActionListener, WorkspaceObserver, PopupMenu.OnMenuItemClickListener {
    private WorkspaceController controller;
    private GhostEditorView methodView;
    private GhostEditorView activityView;
    private GhostEditorView  pauseView, decisionView;
    private GhostEditorView processView, projectView, iterateView;
    private ImageButton saveButton, undoButton, redoButton, settings;
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
        projectView = new GhostEditorView(this, VincaElement.create(VincaElement.ELEMENT_PROJECT));
        processView = new GhostEditorView(this, VincaElement.create(VincaElement.ELEMENT_PROCESS));
        iterateView = new GhostEditorView(this, VincaElement.create(VincaElement.ELEMENT_ITERATE));
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
        settings = (ImageButton) findViewById(R.id.settings);
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

        settings.setOnClickListener(this);
        //backButton.setOnClickListener(this);
        projectNameBar.setOnEditorActionListener(this);

        //trashBin.setClickable(false);

        undoButton = (ImageButton) findViewById(R.id.buttonUndo);
        redoButton = (ImageButton) findViewById(R.id.buttonRedo);
        undoButton.setOnClickListener(this);
        redoButton.setOnClickListener(this);

        scrollView.setOnDragListener(this);


    }

    @Override
    public void onClick(View view) {

        if (view instanceof GhostEditorView) {
            controller.addVincaElement(((GhostEditorView) view).getVincaElement());
        }

        if(view == settings){
            PopupMenu popup = new PopupMenu(EditorActivity.this, settings);
            popup.getMenuInflater().inflate(R.menu.editor_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

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
        this.canvas.removeAllViews();
        VincaViewFabricator fabricator = new VincaViewFabricator(this, controller);
        for (Container vincaRoot : controller.workspace.projects) {
            View root = fabricator.getVincaView(vincaRoot);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT
                            , LinearLayout.LayoutParams.WRAP_CONTENT);
            root.setLayoutParams(lp);
            this.canvas.addView(root);
        }
        this.canvas.invalidate();
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                if (v == scrollView) {
                    try {
                        VincaElementView draggedView = (VincaElementView) event.getLocalState();
                        Container element = (Container) draggedView.getVincaElement();
                        if (element.type == VincaElement.ELEMENT_PROJECT) {
                            controller.addProject(element);
                        }
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
                }

            case DragEvent.ACTION_DRAG_ENDED:
                View draggedView = (View) event.getLocalState();
                draggedView.setVisibility(View.VISIBLE);
                if (!(draggedView instanceof GhostEditorView)) {
                    try {
                        draggedView.setOnDragListener((VincaElementView) draggedView);
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
                }
                break;
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

        LinearLayout viewToExport = new LinearLayout(this);
        viewToExport.setOrientation(LinearLayout.VERTICAL);
        viewToExport.setBackgroundColor(Color.WHITE);

        VincaViewFabricator fabricator = new VincaViewFabricator(this, controller);
        for (Container vincaRoot : controller.workspace.projects) {
            View root = fabricator.getVincaView(vincaRoot);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT
                            , LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(20, 0, 20, 0);
            root.setLayoutParams(lp);
            viewToExport.addView(root);
        }

        viewToExport.getChildAt(0).setBackgroundColor(0);

        ExportDialog exportDialog = new ExportDialog();
        exportDialog.setExportTarget(this, viewToExport, controller.getWorkspaceTitle());
        exportDialog.show(getFragmentManager(), "Export as");

        controller.setCursor(cursor);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_export:
                canvasToJPG();
                break;

            case R.id.item_saveas:
                SaveAsDialog savepop = new SaveAsDialog();
                savepop.setCurrentWorkspace(controller.workspace);
                savepop.show(getFragmentManager(),"save as");
                break;

            default:


                break;
        }

        return true;

    }
}
