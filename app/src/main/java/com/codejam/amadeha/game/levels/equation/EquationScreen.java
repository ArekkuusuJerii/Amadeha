package com.codejam.amadeha.game.levels.equation;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.support.test.espresso.core.internal.deps.guava.collect.ImmutableSet;
import android.support.test.espresso.core.internal.deps.guava.collect.Lists;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.core.Triplet;
import com.codejam.amadeha.game.core.intefaze.IDragListener;
import com.codejam.amadeha.game.core.intefaze.IDropListener;
import com.codejam.amadeha.game.core.intefaze.ITickable;
import com.codejam.amadeha.game.core.widget.DragListener;
import com.codejam.amadeha.game.core.widget.DropListener;
import com.codejam.amadeha.game.core.widget.GameInstructionDialog;
import com.codejam.amadeha.game.data.registry.Game;
import com.codejam.amadeha.game.data.registry.LevelRegistry;
import com.codejam.amadeha.game.data.settings.MusicHelper;
import com.codejam.amadeha.game.levels.LevelBase;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This file was created by Snack on 07/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class EquationScreen extends LevelBase implements ITickable, IDragListener<Button>, IDropListener<TextView, Button> {

    private static final int[][] instructions = {
            {R.string.equation_description},
            {R.string.equation_0, R.drawable.equation_0},
            {R.string.equation_1, R.drawable.equation_1},
            {R.string.equation_2, R.drawable.equation_2},
            {R.string.equation_3, R.drawable.equation_3}
    };
    private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
    );
    private final List<Equation> equations = LevelRegistry.getShuffledRegistry(getGame());
    private LinearLayout equationLayout;
    private MusicHelper.Sound drag;
    private MusicHelper.Sound drop;
    private Set<Button> buttons;
    private Animation explode;

    private Map<String, Triplet<String, String, String>> answerData;
    private List<TextView> unknowns;
    private List<TextView> views;
    private Equation equation;
    private boolean canMove;
    private int answered;
    private int score;

    @Override
    public void init() {
        equationLayout = findViewById(R.id._equation_layout);
        drag = MusicHelper.load(getBaseContext(), MusicHelper.SoundType.EFFECT, R.raw.drag_op);
        drop = MusicHelper.load(getBaseContext(), MusicHelper.SoundType.EFFECT, R.raw.drop_op);
        explode = AnimationUtils.loadAnimation(getBaseContext(), R.anim.vaporise_out);
        explode.setDuration(2000);
        ImmutableSet.Builder<Button> builder = new ImmutableSet.Builder<>();
        for (int i = 0; i < 5; i++) {
            int id = getResources().getIdentifier("answer_" + i, "id", getPackageName());
            View view = findViewById(id);
            view.setOnTouchListener(new DragListener(this));
            builder.add((Button) view);
        }
        buttons = builder.build();
    }

    @Override
    public int getInstruction() {
        return R.string.objetivoUnidadTresTeoria;
    }

    @Override
    public void showInstructions() {
        GameInstructionDialog.create(this, instructions).show();
    }

    @Override
    public void onDrag(Button drag) {
        this.drag.play();
    }

    @Override
    public void onDrop(TextView target, Button drop) {
        if(!canMove) return;
        drop.startAnimation(explode);
        this.drop.play();
        String tag = (String) target.getTag();
        String key = drop.getText().toString();
        if (key.equals(answerData.get(tag).second)) {
            correct.play();
            answerAllDrops(tag);
            if (answered >= unknowns.size()) {
                score += equation.score;
                nextEquation();
            }
        } else {
            for (String s : answerData.keySet()) {
                answerAllDrops(s);
            }
            score -= (int) ((float) equation.score * 0.5F);
            incorrect.play();
            nextEquation();
        }
    }

    @Override
    public void skip(View view) {
        if(!canMove) return;
        for (String s : answerData.keySet()) {
            answerAllDrops(s);
        }
        score -= 10;
        lose.play();
        nextEquation();
    }

    private void answerAllDrops(String key) {
        Triplet<String, String, String> triplet = answerData.get(key);
        for (TextView view : unknowns) {
            String tag = (String) view.getTag();
            if (tag.equals(key) && !view.getText().equals(triplet.second)) {
                view.setText(String.format(triplet.third, triplet.second));
                answered++;
            }
        }
        scaleEquation();
    }

    private void nextEquation() {
        canMove = false;
        stopCountdown();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iterate();
            }
        }, 2000);
    }

    @Override
    public void play() {
        answered = 0;
        canMove = true;
        equationLayout.removeAllViewsInLayout();
        if(level < 10) {
            equation = equations.get(level);
            startCountdown((long) equation.time * 1000L);
            setupEquation();
            level++;
        } else {
            canMove = false;
            win.play();
            setGameOver();
        }
    }

    private void setupEquation() {
        String format = getText(R.string.equation_solve).toString();
        String type = getText(equation.type.name).toString();
        ((TextView) findViewById(R.id.equation_type)).setText(String.format(format, type));
        answerData = equation.getAnswerMap();
        //Setup equation string
        List<String> mappedText = equation.getMappedString();
        views = Lists.newArrayList();
        unknowns = Lists.newArrayList();
        for (String map : mappedText) {
            if (answerData.containsKey(map)) {
                TextView view = addSimpleText(answerData.get(map).first);
                view.setOnDragListener(new DropListener(this));
                view.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                view.setTextColor(Color.DKGRAY);
                view.setGravity(Gravity.CENTER);
                view.setTag(map);
                unknowns.add(view);
                views.add(view);
            } else {
                views.add(addSimpleText(map));
            }
        }
        //Scale equation to screen
        scaleEquation();
        //Setup answers
        List<String> answers = equation.getRandomAnswers();
        int index = 0;
        for (Button button : buttons) {
            String answer = answers.get(index++);
            button.setText(answer);
        }
    }

    private void scaleEquation() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        while (getWidthEquation() >= size.x) {
            for (TextView view : views) {
                view.setTextSize(view.getTextSize() * 0.6F);
            }
        }
    }

    private int getWidthEquation() {
        int pixels = 0;
        for (TextView view : views) {
            view.measure(0, 0);
            pixels += view.getMeasuredWidth();
        }
        return pixels;
    }

    private TextView addSimpleText(String line) {
        TextView view = new TextView(this);
        view.setGravity(Gravity.CENTER);
        view.setText(line);
        view.setTextSize(120);
        view.setMaxLines(1);
        equationLayout.addView(view, params);

        return view;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getView() {
        return R.layout.activity_equation;
    }

    @Override
    public Game getGame() {
        return Game.EQUATION;
    }

    @Override
    public void onCountdownTick(long remaining) {

    }

    @Override
    public void onCountdownFinish() {
        if(!isGameOver()) {
            canMove = false;
            incorrect.play();
            for (String key : answerData.keySet()) {
                answerAllDrops(key);
            }
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
