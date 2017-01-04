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

    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy h:mm");
    private LoadAdapter listadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        listadapter = new LoadAdapter(getFilesDir());
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

    public class LoadAdapter extends BaseAdapter implements View.OnClickListener
    {
        private class ViewCache{
            public TextView titel;
            public TextView date;
        }
        private File sourceDir;
        private File[] directory;
        public void FilterDirectory(final String query){
            Log.d("LoadActivity", "Applying filter " + query);
            final Pattern regex = Pattern.compile("(\\w)*" + query + "(\\w)*");
            directory = sourceDir.listFiles(new FilenameFilter() {


                @Override
                public boolean accept(File dir, String name) {
                    Matcher match = regex.matcher(name);
                    return match.find();
                }
            }
            );
            UpdateDirectory();
        }
        public void UpdateDirectory()
        {
            if(directory==null){
                directory = new File[0]; //Instead of null we pass on an empty array
            }
            Arrays.sort(directory, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    return (int)Math.signum(f1.lastModified() - f2.lastModified());
                }
            });
            notifyDataSetChanged();
        }
        public LoadAdapter(File f)
        {
            sourceDir = f;
            directory = sourceDir.listFiles();
            UpdateDirectory();
        }
        @Override
        public int getCount() {
            return directory.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewCache cache;
            if(convertView==null)
            {
                convertView = getLayoutInflater().inflate(R.layout.loadmenulistelements, null);
                cache = new ViewCache();
                cache.titel = (TextView)convertView.findViewById(R.id.elementName);
                cache.date = (TextView)convertView.findViewById(R.id.elementDate);
                convertView.setTag(R.id.VIEW_CACHE_DATA, cache);
            }
            else
            {
                cache = (ViewCache)convertView.getTag(R.id.VIEW_CACHE_DATA);
            }
            File currentFile = directory[position];
            cache.titel.setText(currentFile.getName());
            cache.date.setText(dateFormatter.format(new Date(currentFile.lastModified())));
            convertView.setTag(R.id.FILE_ENTRY_ID, currentFile);
            return convertView;
        }

        @Override
        public void onClick(View view) {
            /*View row = (View)view.getTag(R.id.LOAD_MENU_ROW_ELEMENT);
            TextView name = (TextView)row.findViewById(R.id.elementName);
            Toast toast;
            if((Integer)view.getTag(R.id.LOAD_MENU_DELETE)!=null)
            {
                toast = Toast.makeText(getApplicationContext(), name.getText() + " is now deleted", Toast.LENGTH_SHORT);
            }
            else
            {
                toast = Toast.makeText(getApplicationContext(), name.getText() + " is now  \"opened\"", Toast.LENGTH_SHORT);
                Intent workspace = new Intent(getApplicationContext(), EditorActivity.class);
                startActivity(workspace);

            }
            toast.show();*/
        }
    }
}
