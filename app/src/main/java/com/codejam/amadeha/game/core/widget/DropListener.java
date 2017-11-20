package com.codejam.amadeha.game.core.widget;

import android.view.DragEvent;
import android.view.View;

import com.codejam.amadeha.game.core.intefaze.IDropListener;

/**
 * This file was created by Snack on 05/03/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */
public class DropListener implements View.OnDragListener {

    private IDropListener listener;

    public DropListener() {}

    public DropListener(IDropListener listener) {
        this.listener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onDrag(View dropTarget, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                View dropped = (View) event.getLocalState();

                if (listener != null)
                    listener.onDrop(dropTarget, dropped);
                break;
            default:
                break;
        }
        return true;
    }
}
