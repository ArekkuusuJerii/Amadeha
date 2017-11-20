package com.codejam.amadeha.game.core.widget;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

import com.codejam.amadeha.game.core.intefaze.IDragListener;

/**
 * This file was created by Snack on 05/03/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */
public class DragListener implements View.OnTouchListener {

    private IDragListener dragListener;

    public DragListener() {}

    public DragListener(IDragListener dragListener) {
        this.dragListener = dragListener;
    }

    @SuppressWarnings("unchecked, deprecation")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.performClick();
            if (dragListener != null)
                dragListener.onDrag(view);
            return true;
        } else {
            return false;
        }
    }
}
