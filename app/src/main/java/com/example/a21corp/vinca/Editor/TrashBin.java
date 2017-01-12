package com.example.a21corp.vinca.Editor;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import com.example.a21corp.vinca.vincaviews.VincaElementView;

/**
 * Created by root on 1/6/17.
 */

public class TrashBin extends View implements View.OnClickListener, View.OnDragListener {

    public TrashBin(Context context) {
        super(context);
        setOnDragListener(this);
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
                setBackgroundColor(0);
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
                setBackgroundColor(Color.RED);
                break;
            }
            case DragEvent.ACTION_DRAG_EXITED:
            {
                setBackgroundColor(0);
                break;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {

    }
}
