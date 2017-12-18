package com.codejam.amadeha.game.levels.matrix;

import android.graphics.Color;
import android.os.Handler;
import android.support.test.espresso.core.internal.deps.guava.collect.Lists;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.core.intefaze.IDragListener;
import com.codejam.amadeha.game.core.intefaze.IDropListener;
import com.codejam.amadeha.game.core.intefaze.ITickable;
import com.codejam.amadeha.game.core.widget.DragListener;
import com.codejam.amadeha.game.core.widget.DropListener;
import com.codejam.amadeha.game.core.widget.GameInstructionDialog;
import com.codejam.amadeha.game.core.widget.TextMatrix;
import com.codejam.amadeha.game.data.registry.Game;
import com.codejam.amadeha.game.data.registry.LevelRegistry;
import com.codejam.amadeha.game.data.settings.MusicHelper;
import com.codejam.amadeha.game.levels.LevelBase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This file was created by Snack on 07/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class MatrixScreen extends LevelBase implements ITickable, IDropListener<TextView, TextView>, IDragListener<TextView> {

    private static final int[][] instructions = {
            {R.string.matrix_description},
            {R.string.matrix_0, R.drawable.matrix_0},
            {R.string.matrix_1, R.drawable.matrix_1},
            {R.string.matrix_2, R.drawable.matrix_2}
    };
    private final TableRow.LayoutParams startParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
    private final TableRow.LayoutParams endParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
    private final List<Matrix> matrices = LevelRegistry.getShuffledRegistry(getGame());
    private MusicHelper.Sound drag;
    private MusicHelper.Sound drop;
    private TableLayout answerLayout;
    private TextMatrix xField;
    private TextMatrix yField;

    private List<TextView> views;
    private Matrix matrix;
    private boolean canMove;
    private int score;

    @Override
    public void init() {
        startParams.setMarginStart(15);
        endParams.setMarginEnd(15);
        drag = MusicHelper.load(getBaseContext(), MusicHelper.SoundType.EFFECT, R.raw.drag_op);
        drop = MusicHelper.load(getBaseContext(), MusicHelper.SoundType.EFFECT, R.raw.drop_op);
        answerLayout = findViewById(R.id.matrix_answer);
        xField = findViewById(R.id.matrix_x);
        yField = findViewById(R.id.matrix_y);
    }

    @Override
    public void showInstructions() {
        GameInstructionDialog.create(this, instructions).show();
    }

    @Override
    public void onDrag(TextView drag) {
        this.drag.play();
    }

    @Override
    public void onDrop(TextView target, TextView drop) {
        if (!canMove) return;
        this.drop.play();
        String from = target.getText().toString();
        String to = drop.getText().toString();
        target.setText(to);
        drop.setText(from);
        String tag = (String) target.getTag();
        if (isMatrixCorrect()) {
            this.correct.play();
            score += matrix.score;
            for (TextView view : views) {
                view.setOnClickListener(null);
                view.setOnDragListener(null);
            }
            nextMatrix();
        } else if (!tag.equals(to)) {
            this.incorrect.play();
            score -= 1;
        }
    }

    @Override
    public void skip(View view) {
        if(!canMove) return;
        score -= 10;
        lose.play();
        nextMatrix();
    }

    private void nextMatrix() {
        canMove = false;
        stopCountdown();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iterate();
            }
        }, 2500);
    }

    @Override
    public void play() {
        canMove = true;
        views = Lists.newArrayList();
        answerLayout.removeAllViewsInLayout();
        if(level < 10) {
            matrix = matrices.get(level);
            startCountdown(matrix.time * 1000L);
            setupMatrix();
            level++;
        } else {
            canMove = false;
            win.play();
            setGameOver();
        }
    }

    private void setupMatrix() {
        ((TextView) findViewById(R.id._operation)).setText(matrix.operation);
        xField.setMatrixArray(matrix.x);
        yField.setMatrixArray(matrix.y);
        List<String> strings = Lists.newArrayList();
        //Setup views
        for (String[] row : matrix.answer) {
            strings.addAll(Arrays.asList(row));
            addRow(row);
        }
        //Shuffle and apply text
        setRows(strings);
    }

    private void setRows(List<String> strings) {
        Collections.shuffle(strings);
        for (int i = 0; i < views.size(); i++) {
            TextView view = views.get(i);
            view.setText(strings.get(i));
        }

        if (isMatrixCorrect()) {
            setRows(strings);
        }
    }

    private void addRow(String[] row) {
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        for (int i = 0; i < row.length; i++) {
            TextView matrix = new TextView(this);
            matrix.setTextColor(Color.BLACK);
            matrix.setTextSize(70);
            if (i == 0 && i + 1 < row.length) {
                matrix.setLayoutParams(endParams);
                tableRow.addView(matrix, endParams);
            } else if (i > 1 && i + 1 >= row.length) {
                matrix.setLayoutParams(startParams);
                tableRow.addView(matrix, startParams);
            } else {
                tableRow.addView(matrix);
            }
            matrix.setTag(String.valueOf(row[i]));
            views.add(matrix);
            matrix.setOnDragListener(new DropListener(this));
            ((View) matrix).setOnTouchListener(new DragListener(this));
        }
        answerLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
    }

    private void showAnswer() {
        for (TextView view : views) {
            String tag = (String) view.getTag();
            view.setText(tag);
        }
    }

    private boolean isMatrixCorrect() {
        for (TextView view : views) {
            String tag = (String) view.getTag();
            if (!view.getText().equals(tag)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getScore() {
        return score;
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
        if(!isGameOver()) {
            canMove = false;
            lose.play();
            showAnswer();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (level >= 10) {
                        gameover();
                    } else {
                        iterate();
                    }
                }
            }, 5000);
        }
    }
}
