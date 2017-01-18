package com.example.a21corp.vinca;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v4.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.a21corp.vinca.AndroidUtilities.FolderAdapter;
import com.example.a21corp.vinca.AndroidUtilities.OnFileSelectedListener;
import com.example.a21corp.vinca.Editor.EditorActivity;
import com.example.a21corp.vinca.Editor.ProjectManager;
import com.example.a21corp.vinca.LoadMenu.ListWorkspaceFragments;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LoadActivity extends AppCompatActivity implements OnFileSelectedListener {
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy h:mm");
    private FolderAdapter listadapter;
    private FrameLayout workspacePreviewHolder;
    ImageView placeholderImage;
    private String title;
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
        name = name.substring(0, name.length() - 4);
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
        title = title.substring(0, title.length() - 4);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment newWorkspacePreview = new LoadActivity_WorkspaceFragment();
        newWorkspacePreview.setArguments(createBundle(f));
        fragmentTransaction.replace(R.id.LoadActivityWorkspaceFragment, newWorkspacePreview, "ProjectSummary");
        fragmentTransaction.commit();
        Log.v("LoadActivity", "Added fragment");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
