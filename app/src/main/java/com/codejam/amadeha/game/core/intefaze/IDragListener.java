package com.codejam.amadeha.game.core.intefaze;

import android.view.View;

/**
 * This file was created by Snack on 05/03/2017. It's distributed as part of AMADHEA.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/AMADHEA
 * AMADHEA is open source, and is distributed under the MIT licence.
 */

/**
 * Interfaz utilizada por {@link com.codejam.amadeha.game.core.widget.DragListener}
 *
 * @param <T> El {@link View} que activa el listener
 */
public interface IDragListener<T extends View> {

    /**
     * Cuando un {@link View} especificado por el {@link T} se arrastra.
     *
     * @param drag El {@link View} al que se arrastra
     */
    void onDrag(T drag);
}