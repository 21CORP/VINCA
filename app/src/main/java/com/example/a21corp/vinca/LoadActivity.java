package com.example.a21corp.vinca;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a21corp.vinca.AndroidUtilities.FolderAdapter;
import com.example.a21corp.vinca.AndroidUtilities.OnFileSelectedListener;
import com.example.a21corp.vinca.Editor.EditorActivity;
import com.example.a21corp.vinca.Editor.ProjectManager;
import com.example.a21corp.vinca.LoadMenu.ListWorkspaceFragments;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LoadActivity extends AppCompatActivity implements OnFileSelectedListener, ActionMode.Callback {
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy h:mm");
    private FolderAdapter listadapter;
    private List<File> selectedFiles = new ArrayList<>();
    private FrameLayout workspacePreviewHolder;
    ImageView placeholderImage;
    private String title;
    private ActionMode acmode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        handleSearchIntent(getIntent());
        workspacePreviewHolder = (FrameLayout)findViewById(R.id.LoadActivityWorkspaceFragment);
        placeholderImage = (ImageView)findViewById(R.id.vincaIcon);
        //Converts the icon to gray scale
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        placeholderImage.getDrawable().setColorFilter(filter);
        if(getSupportFragmentManager().findFragmentByTag("ProjectSummary") != null)
        {
            placeholderImage.setVisibility(View.GONE);
        }
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("LoadActivity", "Created");
    }

    @Override
    protected void onResume() {
        ListWorkspaceFragments projectList =
                (ListWorkspaceFragments) getSupportFragmentManager()
                .findFragmentById(R.id.LoadActivityWorkspaceList);
        projectList.updateFileList();
        listadapter = (FolderAdapter)projectList.getListAdapter();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("LoadActivity", "Creating options menu");
        getMenuInflater().inflate(R.menu.loadactivitymenu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
        return super.onCreateOptionsMenu(menu);
    }

    public void handleSearchIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            applySearch(intent.getStringExtra(SearchManager.QUERY));
        }
    }

    public void applySearch(String q)
    {
        listadapter.FilterDirectory(q);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleSearchIntent(intent);
    }

    private Bundle createBundle(File f){
        Bundle newBundle = new Bundle();
        String path = f.getAbsolutePath();
        path = path.substring(0, path.lastIndexOf("/"));
        String name = f.getName();
        name = name.replace(".ser", "");
        String size = Long.toString(f.length()/1024); //Kilobytes
        newBundle.putString("title", name);
        newBundle.putString("created", "Unknown");
        newBundle.putString("edited",dateFormatter.format(new Date(f.lastModified())));
        newBundle.putString("size", size + " kB");
        newBundle.putString("path", path);
        return newBundle;
    }

    @Override
    public void onSelected(File f) {
        title = f.getName();
        placeholderImage.setVisibility(View.GONE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment newWorkspacePreview = new LoadActivity_WorkspaceFragment();
        newWorkspacePreview.setArguments(createBundle(f));
        fragmentTransaction.replace(R.id.LoadActivityWorkspaceFragment, newWorkspacePreview, "ProjectSummary");
        fragmentTransaction.commit();
        Log.v("LoadActivity", "Added fragment");

    }

    @Override
    public void OnFileCheckedChanged(File f, boolean checked) {
        if(selectedFiles.isEmpty())
        {
            acmode = startActionMode(this);
        }
        if(checked){
            selectedFiles.add(f);
        }
        else
        {
            selectedFiles.remove(f);
        }
        if(selectedFiles.isEmpty() && acmode != null)
        {
           acmode.finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.loadmenucab, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        final int amount = selectedFiles.size();
        if(item.getItemId()==R.id.delete_workspace)
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogCustom);
            LayoutInflater inflater = this.getLayoutInflater();

            View promptView = inflater.inflate(R.layout.deletewarning, null);
            builder.setView(promptView);

            ((TextView)promptView.findViewById(R.id.deleteMessage)).setText("Warning! You are about to delete " + amount + " projects");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < selectedFiles.size(); i++)
                    {
                        File c = selectedFiles.get(i);
                        File preview = new File(c.getParentFile() + "/previews" + c.getName().replace(".ser", ".png"));
                        preview.delete();
                        c.delete();
                    }
                    listadapter.FilterDirectory(""); // Refreshes the directory
                    Toast.makeText(builder.getContext(), "Deleted " + amount + " items", Toast.LENGTH_LONG);
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    overridePendingTransition(0,0);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            final AlertDialog dialog = builder.create();

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialog) {
                    Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    Button negativeButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                    positiveButton.setTextColor(ContextCompat.getColor(builder.getContext(), R.color.background_material_light_1));
                    negativeButton.setTextColor(ContextCompat.getColor(builder.getContext(), R.color.cancelColor));
                }
            });
            builder.show();


            return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
