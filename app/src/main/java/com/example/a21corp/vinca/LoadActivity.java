package com.example.a21corp.vinca;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;



public class LoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        LoadAdapter adapter = new LoadAdapter();
        ListView view = (ListView)findViewById(R.id.listRecent);
        view.setAdapter(adapter);
    }
    public class LoadAdapter extends BaseAdapter
    {
        private String[] names = {"Proj 1", "test2", "Proj", "Vinca"};


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
            }
            TextView titel = (TextView)convertView.findViewById(R.id.elementName);
            titel.setText(names[position]);
            TextView date = (TextView)convertView.findViewById(R.id.elementDate);
            date.setText(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            Button delete = (Button)convertView.findViewById(R.id.elementDelete);
            Button open = (Button)convertView.findViewById(R.id.elementLoad);
            return convertView;
        }
    }
}
