package com.codejam.amadeha.game.levels.matrix;

import android.widget.Button;
import android.widget.TextView;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.core.intefaze.IDragListener;
import com.codejam.amadeha.game.core.intefaze.IDropListener;
import com.codejam.amadeha.game.core.intefaze.ITickable;
import com.codejam.amadeha.game.data.registry.Game;
import com.codejam.amadeha.game.levels.LevelBase;

/**
 * This file was created by Snack on 07/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class MatrixScreen extends LevelBase implements ITickable, IDropListener<TextView, Button>, IDragListener<Button> {

    @Override
    public void onDrag(Button drag) {

    }

    @Override
    public void onDrop(TextView target, Button drop) {

    }

    @Override
    public void init() {

    }

    @Override
    public void play() {

    }

    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public int getView() {
        return R.layout.activity_matrix;
    }

    @Override
    public Game getGame() {
        return Game.MATRIX;
    }

    @Override
    public void onCountdownTick(long remaining) {

    }

    @Override
    public void onCountdownFinish() {

    }
}
