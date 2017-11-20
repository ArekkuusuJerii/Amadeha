package com.codejam.amadeha.game.core.intefaze;

import android.view.View;

/**
 * This file was created by Snack on 05/03/2017. It's distributed as part of AMADHEA.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/AMADHEA
 * AMADHEA is open source, and is distributed under the MIT licence.
 */

public interface IDropListener<T extends View, D extends View> {

    void onDrop(T target, D drop);
}
