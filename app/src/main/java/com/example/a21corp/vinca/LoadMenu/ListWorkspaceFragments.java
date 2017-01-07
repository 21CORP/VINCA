package com.example.a21corp.vinca.LoadMenu;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.a21corp.vinca.AndroidUtilities.FolderAdapter;
import com.example.a21corp.vinca.AndroidUtilities.OnFileSelectedListener;
import com.example.a21corp.vinca.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListWorkspaceFragments extends ListFragment {

    OnFileSelectedListener selectListener;
    public ListWorkspaceFragments() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        updateFileList();
        return inflater.inflate(R.layout.fragment_list_workspace_fragments, container, false);
    }

    public void updateFileList() {
        FolderAdapter adapter = new FolderAdapter(
                new File(getActivity().getFilesDir() + "/workspaces/"));
        adapter.setOnFileSelectedListener((OnFileSelectedListener)getActivity());
        setListAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        SearchView searchViewAction = (SearchView) menu.findItem(R.id.action_search).getActionView();
        //SearchView searchView = (SearchView) menu.findItem(R.id.action_search);
        searchViewAction.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((FolderAdapter)getListAdapter()).FilterDirectory(newText);
                return true;
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
