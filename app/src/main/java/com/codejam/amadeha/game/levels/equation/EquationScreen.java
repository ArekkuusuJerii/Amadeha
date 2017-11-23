package com.codejam.amadeha.game.levels.equation;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.support.test.espresso.core.internal.deps.guava.collect.ImmutableSet;
import android.support.test.espresso.core.internal.deps.guava.collect.Lists;
import android.util.Pair;
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
import com.codejam.amadeha.game.core.intefaze.IDragListener;
import com.codejam.amadeha.game.core.intefaze.IDropListener;
import com.codejam.amadeha.game.core.intefaze.ITickable;
import com.codejam.amadeha.game.core.widget.DragListener;
import com.codejam.amadeha.game.core.widget.DropListener;
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

    private Map<String, Pair<String, String>> answerMap;
    private List<TextView> unknowns;
    private List<TextView> views;
    private Equation equation;
    private boolean canMove;
    private int answered;
    private int score;

    @Override
    public void init() {
        equationLayout = (LinearLayout) findViewById(R.id._equation_layout);
        drag = MusicHelper.load(getBaseContext(), MusicHelper.SoundType.EFFECT, R.raw.drag_op);
        drop = MusicHelper.load(getBaseContext(), MusicHelper.SoundType.EFFECT, R.raw.drop_op);
        explode = AnimationUtils.loadAnimation(getBaseContext(), R.anim.vaporise_out);
        explode.setDuration(500);
        ImmutableSet.Builder<Button> builder = new ImmutableSet.Builder<>();
        for (int i = 0; i < 5; i++) {
            int id = getResources().getIdentifier("_button_" + i, "id", getPackageName());
            View view = findViewById(id);
            view.setOnTouchListener(new DragListener(this));
            builder.add((Button) view);
        }
        buttons = builder.build();
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
        Pair<String, String> box = answerMap.get(tag);
        String key = drop.getText().toString();
        if (key.equals(box.second)) {
            correct.play();
            answerAllDrops(tag, true);
            if (answered >= unknowns.size()) {
                score += equation.getScore();
                nextEquation();
            }
        } else {
            for (String s : answerMap.keySet()) {
                answerAllDrops(s, false);
            }
            incorrect.play();
            nextEquation();
        }
    }

    private void answerAllDrops(String key, boolean correct) {
        Pair<String, String> box = answerMap.get(key);
        for (TextView view : unknowns) {
            String tag = (String) view.getTag();
            if (tag.equals(key) && !view.getText().equals(box.second)) {
                String format = getString(R.string.bracket_placeholder);
                view.setText(String.format(format, box.second));
                //view.setTextColor(correct ? Color.GREEN : Color.RED);
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
        }, 2500);
    }

    @Override
    public void play() {
        answered = 0;
        canMove = true;
        equationLayout.removeAllViewsInLayout();
        if(level < 10) {
            equation = equations.get(level);
            startCountdown((long) equation.getTime() * 1000L);
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
        String type = getText(equation.getType().name).toString();
        ((TextView) findViewById(R.id.equation_type)).setText(String.format(format, type));
        answerMap = equation.getAnswerMap();
        //Setup equation string
        List<String> mappedText = equation.getMappedString();
        views = Lists.newArrayList();
        unknowns = Lists.newArrayList();
        for (String map : mappedText) {
            if (answerMap.containsKey(map)) {
                TextView view = addSimpleText(answerMap.get(map).first);
                view.setOnDragListener(new DropListener(this));
                view.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                view.setTextColor(Color.DKGRAY);
                view.setGravity(Gravity.CENTER);
                view.setTag(map);
                unknowns.add(view);
            } else {
                TextView view = addSimpleText(map);
                views.add(view);
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
        int pixels = 0;
        for (TextView view : views) {
            pixels += view.getWidth();
        }
        resizeViews(pixels, size.x);
    }

    private void resizeViews(int px, int max) {
        if(px > max) {
            int pixels = 0;
            for (TextView view : views) {
                int width = view.getWidth();
                view.setTextSize(width * (max / px));
                pixels += view.getWidth();
            }
            resizeViews(pixels, max);
        }
    }

    private TextView addSimpleText(String line) {
        TextView view = new TextView(this);
        view.setGravity(Gravity.CENTER);
        view.setText(line);
        view.setTextSize(40);
        equationLayout.addView(view, params);

        return view;
    }

    @Override
    public boolean canResume() {
        return false;
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
            for (String key : answerMap.keySet()) {
                answerAllDrops(key, false);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iterate();
                }
            }, 5000);
        }
    }
}
