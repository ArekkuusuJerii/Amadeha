package com.codejam.amadeha.game.core.widget;

import android.app.Dialog;
import android.content.Context;

/**
 * This file was created by Snack on 30/03/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */
public class DialogWrapper {

    private final Context context;
    private boolean minimizable;
    private boolean cancelable;
    private int style = -1;
    private int feature;
    private int id;

    public DialogWrapper(Context context, int id) {
        this.context = context;
        this.id = id;
    }

    public DialogWrapper setStyle(int style) {
        this.style = style;
        return this;
    }

    public DialogWrapper setFeature(int feature) {
        this.feature = feature;
        return this;
    }

    public DialogWrapper setMinimizable(boolean minimizable) {
        this.minimizable = minimizable;
        return this;
    }

    public DialogWrapper setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public Dialog build(float x, float y) {
        Dialog dialog = style == -1 ? new Dialog(context) : new Dialog(context, style);
        if (feature != 0) {
            dialog.requestWindowFeature(feature);
        }
        dialog.setContentView(id);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(minimizable);
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * x);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * y);
        if (dialog.getWindow() != null)
            dialog.getWindow().setLayout(width, height);
        return dialog;
    }
}
