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
    private WorkspaceController manager;

    public TrashBin(Context context) {
        super(context);
    }
    /*TrashBin(VincaViewManager manager)
    {
        this.manager = manager;
    }*/

    @Override
    public boolean onDrag(View view, DragEvent event) {
        VincaElementView draggedView = null;
        if(event.getAction()!=DragEvent.ACTION_DRAG_ENDED)
        {
            try {
                draggedView = (VincaElementView) event.getLocalState();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
        Log.d("TrashBin", "DraggedView "  + (draggedView!=null));
        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
            {
                //User dropped a view into trash bin?
                Log.d("WorkspaceController", "Dropped on view trashbin! - Deleting");
                view.setBackgroundColor(0);
                ((VincaElementView)draggedView).remove();
                break;
            }
            case DragEvent.ACTION_DRAG_EXITED:
            {
                view.setBackgroundColor(0);
                break;
            }
            case DragEvent.ACTION_DRAG_ENTERED:
            {
                view.setBackgroundColor(Color.RED);
                break;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {

    }
}
