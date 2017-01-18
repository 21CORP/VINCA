package com.example.a21corp.vinca.AndroidUtilities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.a21corp.vinca.R;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FolderAdapter extends BaseAdapter implements View.OnClickListener
{
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy h:mm");
    private static Comparator<File> lastModifiedComparator = new Comparator<File>() {
        @Override
        public int compare(File f1, File f2) {
            return (int)Math.signum(f2.lastModified() - f1.lastModified());
        }
    };
    private class ViewCache{
        public TextView titel;
        public TextView date;
        public Button button;
    }
    private OnFileSelectedListener listener;
    private File sourceDir;
    private File[] directory;

    public void setOnFileSelectedListener(OnFileSelectedListener listener){
        this.listener = listener;
    }
    public void FilterDirectory(final String query){
        Log.d("LoadActivity", "Applying filter " + query);
        final Pattern regex = Pattern.compile(query,Pattern.CASE_INSENSITIVE);
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
        Arrays.sort(directory, lastModifiedComparator);
        notifyDataSetChanged();
    }
    public FolderAdapter(File f)
    {
        sourceDir = f;
        directory = sourceDir.listFiles();
        UpdateDirectory();
    }
    @Override
    public int getCount() {
        return directory == null ? 0 : directory.length;
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.loadmenulistelements, null);
            cache = new ViewCache();
            cache.titel = (TextView)convertView.findViewById(R.id.elementName);
            cache.date = (TextView)convertView.findViewById(R.id.elementDate);
            convertView.setOnClickListener(this);
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
        Log.d("FolderAdapter", "onClickRegistered");
        File selected;
        if((selected = (File)view.getTag(R.id.FILE_ENTRY_ID))!=null){
            if(listener == null){
                return;
            }
            listener.onSelected(selected);
        }
    }
}