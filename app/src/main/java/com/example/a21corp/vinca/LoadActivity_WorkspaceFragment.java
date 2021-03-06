package com.example.a21corp.vinca;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a21corp.vinca.Editor.EditorActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoadActivity_WorkspaceFragment extends Fragment implements View.OnClickListener{
    private TextView title;
    private TextView created;
    private TextView edited;
    private TextView size;
    private Button loadButton;
    private ImageView preview;

    public LoadActivity_WorkspaceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_load_activity_workspace2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = (TextView)view.findViewById(R.id.LoadActivityWorkspaceTitle);
        created = (TextView)view.findViewById(R.id.LoadActivityWorkspaceCreated);
        edited = (TextView)view.findViewById(R.id.LoadActivityWorkspaceEdited);
        size = (TextView)view.findViewById(R.id.LoadActivityWorkspaceSize);
        preview = (ImageView) view.findViewById(R.id.preview);
        loadButton = (Button) view.findViewById(R.id.button_loadSelectedProject);
        loadButton.setOnClickListener(this);
        updateFragment();
    }

    private void showPreview(String title) {
        if (title == null) {
            return;
        }
        Bundle state = getArguments();
        String path = state.getString("path", null);

        if (path == null) {
            return;
        }
        if (path.endsWith("/workspaces")) {
            path = path.substring(0, path.lastIndexOf("/")) + "/previews";
        }
        File image = new File(path + "/" + title + ".png");
        if (image.exists()) {
            preview.setImageURI(Uri.fromFile(image));
        }
    }

    private void updateFragment()
    {
        Bundle state = getArguments();
        title.setText(state.getString("title", "Unknown"));
        created.setText(state.getString("created", "Unknown"));
        edited.setText(state.getString("edited", "Unknown"));
        size.setText(state.getString("size", "Unknown"));
        showPreview(state.getString("title", null));
    }

    @Override
    public void onClick(View v) {
        if (v == loadButton) {
            Intent editor = new Intent(getActivity(), EditorActivity.class);
            editor.putExtra("title", title.getText());
            getActivity().startActivity(editor);
            getActivity().overridePendingTransition( R.anim.slide_up_in, R.anim.slide_up_out);
        }
    }
}
