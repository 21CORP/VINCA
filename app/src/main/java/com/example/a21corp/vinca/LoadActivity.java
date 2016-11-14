package com.example.a21corp.vinca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a21corp.vinca.Editor.EditorActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class LoadActivity extends AppCompatActivity implements View.OnClickListener {

    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy h:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        LoadAdapter listadapter = new LoadAdapter();
        RecentLoadAdapter recentAdapter = new RecentLoadAdapter();
        ListView view = (ListView)findViewById(R.id.listRecent);
        ListView view2 = (ListView)findViewById(R.id.listAll);
        FloatingActionButton floatAButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatAButton.setOnClickListener(this);
        view2.setAdapter(listadapter);
        view.setAdapter(recentAdapter);
    }

    @Override
    public void onClick(View v) {
        CreateMenuPopUp p = new CreateMenuPopUp();
        p.show(getFragmentManager(),"pop");
    }

    public class RecentLoadAdapter extends LoadAdapter
    {
        RecentLoadAdapter(){
            String[] newNames = new String[3];
            System.arraycopy(names, 0, newNames, 0, 3);
            names = newNames;
        }
    }
    public class LoadAdapter extends BaseAdapter implements View.OnClickListener
    {


        protected String[] names = {"Proj 1", "test2", "Proj", "Vinca", "Vinca2", "Festival 666", "Meeting", "Birthday party", "Recipe 2", "App development", "Software"};


        @Override
        public int getCount() {
            return names.length;
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
            if(convertView==null)
            {
                convertView = getLayoutInflater().inflate(R.layout.loadmenulistelements, null);
                ImageButton delete = (ImageButton)convertView.findViewById(R.id.deleteButton);
                ImageButton open = (ImageButton)convertView.findViewById(R.id.editButton);
                delete.setOnClickListener(this);
                open.setOnClickListener(this);
                delete.setTag( R.id.LOAD_MENU_DELETE, new Integer(1)); //1 == should delete
                delete.setTag(R.id.LOAD_MENU_ROW_ELEMENT, convertView);
                open.setTag(R.id.LOAD_MENU_OPEN, new Integer(1));
                open.setTag(R.id.LOAD_MENU_ROW_ELEMENT, convertView);
            }
            TextView titel = (TextView)convertView.findViewById(R.id.elementName);
            titel.setText(names[position]);
            TextView date = (TextView)convertView.findViewById(R.id.elementDate);
            Calendar c = Calendar.getInstance();

            date.setText(dateFormatter.format(c.getTime()));

            return convertView;
        }

        @Override
        public void onClick(View view) {
            View row = (View)view.getTag(R.id.LOAD_MENU_ROW_ELEMENT);
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
            toast.show();
        }
    }
}
