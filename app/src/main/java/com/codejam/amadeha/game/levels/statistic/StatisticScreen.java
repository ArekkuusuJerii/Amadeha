package com.codejam.amadeha.game.levels.statistic;

import android.graphics.Color;
import android.os.Handler;
import android.support.test.espresso.core.internal.deps.guava.collect.Lists;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.core.intefaze.ITickable;
import com.codejam.amadeha.game.core.widget.AutoResizeTextView;
import com.codejam.amadeha.game.core.widget.SimpleTouchListener;
import com.codejam.amadeha.game.data.registry.Game;
import com.codejam.amadeha.game.data.registry.LevelRegistry;
import com.codejam.amadeha.game.levels.LevelBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This file was created by Snack on 07/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class StatisticScreen extends LevelBase implements ITickable {

    private final List<Statistic> statistics = LevelRegistry.getShuffledRegistry(getGame());
    private TableLayout tableLayout;
    private TextView question;
    private Animation poof;

    private List<AutoResizeTextView> views;
    private Statistic statistic;
    private boolean canAnswer;
    private int score;

    @Override
    public void init() {
        poof = AnimationUtils.loadAnimation(getBaseContext(), R.anim.vaporise_text);
        poof.setDuration(2000);
        tableLayout = findViewById(R.id.answers);
        question = findViewById(R.id.question);
        startCountdown(100000);
    }

    public void selectAnswer(View view) {
        if(!canAnswer) return;
        Boolean bool = (Boolean) view.getTag();
        if(bool) {
            score += 10;
            correct.play();
        } else {
            score -= 5;
            incorrect.play();
        }
        nextStatistic();
    }

    @Override
    public void skip(View view) {
        if(!canAnswer) return;
        score -= 10;
        lose.play();
        nextStatistic();
    }

    private void nextStatistic() {
        canAnswer = false;
        showAnswer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iterate();
            }
        }, 2000);
    }

    private void showAnswer() {
        for (AutoResizeTextView view : views) {
            Boolean bool = (Boolean) view.getTag();
            view.setClickable(false);
            if(!bool) {
                view.setTextColor(Color.GRAY);
                view.startAnimation(poof);
            }
        }
    }

    @Override
    public void play() {
        canAnswer = true;
        tableLayout.removeAllViewsInLayout();
        if (level < 10) {
            statistic = statistics.get(level);
            setupStatistic();
            level++;
        } else {
            canAnswer = false;
            win.play();
            setGameOver();
        }
    }

    private void setupStatistic() {
        question.setText(statistic.question);
        views = Lists.newArrayList();
        List<String> strings = new ArrayList<>(Arrays.asList(statistic.answers));
        Collections.shuffle(strings);
        if (strings.size() > 4) {
            strings = strings.subList(0, 4);
        }
        String none = getString(R.string.none_of_the_above);
        boolean override = statistic.random && rand.nextFloat() <= 0.8F;
        if (override) {
            int index = rand.nextInt(strings.size());
            strings.set(index, statistic.answer);
            if(rand.nextFloat() <= 0.6F) {
                strings.add(none);
            }
        } else {
            strings.add(none);
        }
        for (int i = 0; i < strings.size(); i++) {
            String answer = strings.get(i);
            addRow(i, answer, !override ? answer.equals(none) : answer.equals(statistic.answer));
        }
    }

    private void addRow(int row, String answer, boolean isAnswer) {
        TableRow view = (TableRow) getLayoutInflater().inflate(R.layout.statistic_row, null);
        ((TextView) view.findViewById(R.id.number)).setText(String.format("%o.-", row));
        AutoResizeTextView text = view.findViewById(R.id.text);
        ((View) text).setOnTouchListener(new SimpleTouchListener() {
            @Override
            public void touchLift(View v) {
                selectAnswer(v);
            }
        });
        text.setText(answer);
        text.setTag(isAnswer);
        views.add(text);
        tableLayout.addView(view, new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getView() {
        return R.layout.activity_statistic;
    }

    @Override
    public Game getGame() {
        return Game.STATISTIC;
    }

    @Override
    public void onCountdownTick(long remaining) {

    }

    @Override
    public void onCountdownFinish() {
        canAnswer = false;
        lose.play();
        showAnswer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gameover();
            }
        }, 2000);
    }
}
