package com.example.a21corp.vinca;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoadActivity_WorkspaceFragment extends Fragment {
    private TextView title;
    private TextView created;
    private TextView edited;

    public LoadActivity_WorkspaceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_load_activity_workspace, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = (TextView)view.findViewById(R.id.LoadActivityWorkspaceTitle);
        created = (TextView)view.findViewById(R.id.LoadActivityWorkspaceCreated);
        edited = (TextView)view.findViewById(R.id.LoadActivityWorkspaceEdited);
        updateFragment();
    }

    private void updateFragment()
    {
        Bundle state = getArguments();
        title.setText(state.getString("title", "Unknown"));
        created.setText(state.getString("created", "Unknown"));
        edited.setText(state.getString("edited", "Unknown"));
    }
}
