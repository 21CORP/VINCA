package com.example.a21corp.vinca;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItem;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a21corp.vinca.AndroidUtilities.FolderAdapter;
import com.example.a21corp.vinca.AndroidUtilities.OnFileSelectedListener;
import com.example.a21corp.vinca.Editor.EditorActivity;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoadActivity extends AppCompatActivity implements View.OnClickListener {

    private FolderAdapter listadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        listadapter = new FolderAdapter(getFilesDir(), LoadActivity.this);
        handleSearchIntent(getIntent());
        ListView view = (ListView)findViewById(R.id.workspaceList);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        view.setAdapter(listadapter);
        Log.d("LoadActivity", "Created");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("LoadActivity", "Creating options menu");
        getMenuInflater().inflate(R.menu.loadactivitymenu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //Bugged? Never called..
        /*searchManager.setOnDismissListener(new SearchManager.OnDismissListener() {
            @Override
            public void onDismiss() {
                Log.d("LoadActivity", "Hello?");
                listadapter.FilterDirectory("");
            }
        });*/
        SearchView searchViewAction = (SearchView) menu.findItem(R.id.action_search).getActionView();
        //SearchView searchView = (SearchView) menu.findItem(R.id.action_search);
        searchViewAction.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listadapter.FilterDirectory(newText);
                return true;
            }
        });
        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
        return super.onCreateOptionsMenu(menu);
    }

    public void handleSearchIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            applySearch(intent.getStringExtra(SearchManager.QUERY));
            Log.d("LoadActivity", "Handling search intent");
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

    @Override
    public void onClick(View v) {
        CreateMenuPopUp p = new CreateMenuPopUp();
        p.show(getFragmentManager(),"pop");
    }


}
