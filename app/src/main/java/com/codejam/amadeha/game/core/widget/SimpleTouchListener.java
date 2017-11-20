package com.codejam.amadeha.game.core.widget;

import android.view.MotionEvent;
import android.view.View;

/**
 * This file was created by Snack on 15/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class SimpleTouchListener implements View.OnTouchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.performClick();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                touchLift(v);
                break;
            case MotionEvent.ACTION_DOWN:
                touchPush(v);
                break;
        }
        return true;
    }

    public void touchLift(View v) {
        //For rent
    }

    public void touchPush(View v) {
        //For rent
    }
}
