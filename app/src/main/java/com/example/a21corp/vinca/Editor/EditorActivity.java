package com.example.a21corp.vinca.Editor;

import android.graphics.Color;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.a21corp.vinca.AutoSaver;
import com.example.a21corp.vinca.AutosaveObserver;
import com.example.a21corp.vinca.HistoryManagement.AddProjectCommand;
import com.example.a21corp.vinca.HistoryManagement.CopyCommand;
import com.example.a21corp.vinca.HistoryManagement.CreateCommand;
import com.example.a21corp.vinca.HistoryManagement.CutCommand;
import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.HistoryManagement.MoveCommand;
import com.example.a21corp.vinca.HistoryManagement.PasteCommand;
import com.example.a21corp.vinca.R;
import com.example.a21corp.vinca.SaveAsDialog;
import com.example.a21corp.vinca.elements.Container;
import com.example.a21corp.vinca.elements.Element;
import com.example.a21corp.vinca.elements.Node;
import com.example.a21corp.vinca.elements.VincaActivity;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.vincaviews.ContainerView;
import com.example.a21corp.vinca.vincaviews.VincaElementView;
import com.example.a21corp.vinca.vincaviews.VincaViewFabricator;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class EditorActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener, View.OnDragListener, AutosaveObserver
        , View.OnTouchListener, TextView.OnEditorActionListener, WorkspaceObserver, PopupMenu.OnMenuItemClickListener {
    private WorkspaceController controller;
    private GhostEditorView methodView;
    private GhostEditorView activityView;
    private GhostEditorView  pauseView, decisionView;
    private GhostEditorView processView, projectView, iterateView;
    private ImageButton saveButton, undoButton, redoButton, settings;
    private ImageButton trashBin, copyButton, cutBotton, pasteButton;
    private EditText projectNameBar;
    private TextView saveStatusBar;
    public LinearLayout canvas;
    public HorizontalScrollView hScroll;
    public ScrollView vScroll;
    public LinearLayout elementPanel;

    private Historian historian;

    private String dirPath;
    private File projDir;
    private AutoSaver autoSaver;


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
        initiateScrollViews();
        trashBin.setOnDragListener(new TrashBin(this));
        historian = Historian.getInstance();

        if(autoSaver == null){
            autoSaver = new AutoSaver(controller.workspace, dirPath);
            autoSaver.observerList.add(this);
        }
        autoSaver.save();

    }

    private void initiateScrollViews() {
        vScroll.setOnTouchListener(new View.OnTouchListener() { //inner scroll listener
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        hScroll.setOnTouchListener(new View.OnTouchListener() { //outer scroll listener
            private float mx, my, curX, curY;
            private boolean started = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                curX = event.getX();
                curY = event.getY();
                int dx = (int) (mx - curX);
                int dy = (int) (my - curY);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (started) {
                            vScroll.scrollBy(0, dy);
                            hScroll.scrollBy(dx, 0);
                        } else {
                            started = true;
                        }
                        mx = curX;
                        my = curY;
                        break;
                    case MotionEvent.ACTION_UP:
                        vScroll.scrollBy(0, dy);
                        hScroll.scrollBy(dx, 0);
                        started = false;
                        break;
                }
                return false;
            }
        });
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
            String loadPath = getIntent().getExtras().getString("dirPath", dirPath);
            workspace = ProjectManager.loadProject(loadPath + "/" + title + ".ser");
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
        hScroll = (HorizontalScrollView) findViewById(R.id.hScrollView);
        vScroll = (ScrollView) findViewById(R.id.vScrollView);
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

        //MISC Views
        saveButton = (ImageButton) findViewById(R.id.saveas) ;
        settings = (ImageButton) findViewById(R.id.settings);
        trashBin = (ImageButton) findViewById(R.id.trashbin);
        projectNameBar = (EditText) findViewById(R.id.text_project_name);
        saveStatusBar = (TextView) findViewById(R.id.text_save_status);
        copyButton = (ImageButton) findViewById(R.id.buttonCopy);
        cutBotton = (ImageButton) findViewById(R.id.buttonCut);
        pasteButton = (ImageButton) findViewById(R.id.buttonPaste);

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
        copyButton.setOnClickListener(this);
        cutBotton.setOnClickListener(this);
        pasteButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        settings.setOnClickListener(this);
        //backButton.setOnClickListener(this);
        projectNameBar.setOnEditorActionListener(this);

        //trashBin.setClickable(false);

        undoButton = (ImageButton) findViewById(R.id.buttonUndo);
        redoButton = (ImageButton) findViewById(R.id.buttonRedo);
        undoButton.setOnClickListener(this);
        redoButton.setOnClickListener(this);

        hScroll.setOnDragListener(this);


    }

    @Override
    public void onClick(View view) {

        if (view instanceof GhostEditorView) {
            //controller.addVincaElement(((GhostEditorView) view).getVincaElement());
            VincaElement element = ((GhostEditorView) view).getVincaElement();
            if (InputValidator.moveIsValid(element, controller.getCursor())) {
                CreateCommand createCmd = new CreateCommand(element, controller);
                Historian.getInstance().storeAndExecute(createCmd);
            }
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
        if(view == copyButton){
            copy();
        }
        if (view == cutBotton) {
            cut();
        }
        if (view == pasteButton) {
            paste();
        }
    }

    private void paste() {
        int index;
        Element cursor = controller.getCursor();
        if (cursor instanceof Container) {
            index = ((Container) cursor).containerList.size();
        }
        else if (cursor instanceof VincaActivity) {
            index = ((VincaActivity) cursor).nodes.size();
            cursor = cursor.getParent();
        }
        else {
            return;
        }
        historian.storeAndExecute(new PasteCommand(cursor, index, controller));
    }

    private void cut() {
        VincaElement element = controller.getCursor();
        Element parent = element.getParent();
        historian.storeAndExecute(new CutCommand(element, parent, controller));
    }

    private void copy() {
        VincaElement element = controller.getCursor();
        historian.storeAndExecute(new CopyCommand(element, controller));
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
                if (v == hScroll) {
                    try {
                        VincaElementView draggedView = (VincaElementView) event.getLocalState();
                        Container element = (Container) draggedView.getVincaElement();
                        if (element.type == VincaElement.ELEMENT_PROJECT) {
                            Container parent = element.parent;
                            Historian.getInstance().storeAndExecute
                                    (new AddProjectCommand(element, parent, controller));
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
        autoSaver.save();
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
        exportDialog.setExportTarget(this, viewToExport, controller.getWorkspaceTitle(), controller.workspace);
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

    @Override
    public void saveStatus(boolean status) {
        if(status){
            saveStatusBar.setText("Saving...");
            }
        else{
            saveStatusBar.setText("Saved");
        }
    }
}
