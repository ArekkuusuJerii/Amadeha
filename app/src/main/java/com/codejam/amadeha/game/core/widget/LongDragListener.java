package com.codejam.amadeha.game.core.widget;

import android.content.ClipData;
import android.view.View;

import com.codejam.amadeha.game.core.intefaze.IClickListener;

/**
 * This file was created by Snack on 08/03/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */
public class LongDragListener implements View.OnLongClickListener {

    private IClickListener dragListener;

    public LongDragListener() {}

    public LongDragListener(IClickListener dragListener) {
        this.dragListener = dragListener;
    }

    @SuppressWarnings("unchecked, deprecation")
    @Override
    public boolean onLongClick(View view) {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(data, shadowBuilder, view, 0);
        if (dragListener != null)
            dragListener.onClick(view);
        return true;
    }
}
