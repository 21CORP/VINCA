package com.example.a21corp.vinca.Editor;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import com.example.a21corp.vinca.HistoryManagement.DeleteCommand;
import com.example.a21corp.vinca.HistoryManagement.Historian;
import com.example.a21corp.vinca.elements.VincaElement;
import com.example.a21corp.vinca.vincaviews.VincaElementView;

/**
 * Created by root on 1/6/17.
 */

public class TrashBin implements View.OnClickListener, View.OnDragListener {

    private final EditorActivity editorActivity;

    public TrashBin(EditorActivity editorActivity, View view) {
        this.editorActivity = editorActivity;
        view.setOnDragListener(this);
        view.setOnClickListener(this);
    }
    /*TrashBin(VincaViewManager manager)
    {
        this.manager = manager;
    }*/

    @Override
    public boolean onDrag(View view, DragEvent event) {
        VincaElementView draggedView = null;
        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
            {
                //User dropped a view into trash bin?
                view.setBackgroundColor(0);
                try {
                    draggedView = (VincaElementView) event.getLocalState();
                    Log.d("WorkspaceController", "Dropped on view trashbin! - Deleting");
                    draggedView.remove();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
                break;
            }
            case DragEvent.ACTION_DRAG_ENTERED:
            {
                view.setBackgroundColor(Color.RED);
                break;
            }
            case DragEvent.ACTION_DRAG_EXITED:
            {
                view.setBackgroundColor(0);
                break;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        VincaElement cursor = editorActivity.controller.getCursor();
        Historian.getInstance().storeAndExecute
                (new DeleteCommand(cursor, cursor.parent, editorActivity.controller));
    }
}
