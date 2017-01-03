package com.example.a21corp.vinca;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a21corp.vinca.Editor.EditorActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;


public class LoadActivity extends AppCompatActivity implements View.OnClickListener {

    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy h:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        LoadAdapter listadapter = new LoadAdapter(getFilesDir());
        ListView view = (ListView)findViewById(R.id.workspaceList);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // ListView view2 = (ListView)findViewById(R.id.listAll);
        //FloatingActionButton floatAButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        //floatAButton.setOnClickListener(this);
        //view2.setAdapter(listadapter);
        view.setAdapter(listadapter);
        Log.d("LoadActivity", "Created");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("LoadActivity", "Creating options menu");
        getMenuInflater().inflate(R.menu.loadactivitymenu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
        Log.d("LoadActivity", "SearchInfo: " + info + " for: " + getComponentName());
        searchView.setSearchableInfo(info);
        //searchView.setIconifiedByDefault(false);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("LoadActivity", "in onNewIntent");
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

       private File[] directory;

        public LoadAdapter(File f)
        {
            directory = f.listFiles();
            if(directory==null){
                directory = new File[0]; //Instead of null we pass on an empty array
            }
            Arrays.sort(directory, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    return (int)Math.signum(f1.lastModified() - f2.lastModified());
                }
            });
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
