package com.codejam.amadeha.game.levels.sets;

import android.os.Handler;
import android.support.test.espresso.core.internal.deps.guava.collect.ImmutableSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
import com.codejam.amadeha.game.levels.sets.Sets.SetType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This file was created by Snack on 07/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class SetsScreen extends LevelBase implements ITickable, IDragListener<ImageView>, IDropListener<TextView, ImageView> {

    private final List<Sets> setsList = LevelRegistry.getShuffledRegistry(getGame());
    private MusicHelper.Sound drag;
    private MusicHelper.Sound drop;
    private Animation fade;
    private Set<ImageView> answers;
    private TextView text;
    private ImageView image;

    private Sets sets;
    private SetType answer;
    private boolean canAnswer;
    private int score;

    @Override
    public void init() {
        drag = MusicHelper.load(getBaseContext(), MusicHelper.SoundType.EFFECT, R.raw.drag_op);
        drop = MusicHelper.load(getBaseContext(), MusicHelper.SoundType.EFFECT, R.raw.drop_op);
        fade = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade);
        fade.setDuration(2000);
        text = (TextView) findViewById(R.id.question);
        text.setOnDragListener(new DropListener(this));
        image = (ImageView) findViewById(R.id.image);
        ImmutableSet.Builder<ImageView> builder = new ImmutableSet.Builder<>();
        ImageView none = (ImageView) findViewById(R.id.none);
        none.setTag(SetType.NONE);
        builder.add(none);
        ImageView union = (ImageView) findViewById(R.id.union);
        union.setTag(SetType.UNION);
        builder.add(union);
        ImageView empty = (ImageView) findViewById(R.id.empty);
        empty.setTag(SetType.EMPTY);
        builder.add(empty);
        ImageView inter = (ImageView) findViewById(R.id.intersection);
        inter.setTag(SetType.INTERSECTION);
        builder.add(inter);
        answers = builder.build();
        DragListener listener = new DragListener(this);
        for (View view : answers) {
            view.setOnTouchListener(listener);
        }
        startCountdown(500000);
    }

    @Override
    public void onDrag(ImageView drag) {
        this.drag.play();
    }

    @Override
    public void onDrop(TextView target, ImageView drop) {
        if (!canAnswer) return;
        this.drop.play();
        SetType set = (SetType) drop.getTag();
        if (this.answer == set) {
            this.correct.play();
            score += 5;
        } else {
            this.incorrect.play();
            score -= 5;
        }
        nextSet();
    }

    private void nextSet() {
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
        for (ImageView image : answers) {
            SetType set = (SetType) image.getTag();
            if (answer != set) {
                image.startAnimation(fade);
            }
        }
    }

    @Override
    public void play() {
        canAnswer = true;
        if (level < 10) {
            sets = setsList.get(level);
            setupSet();
            level++;
        } else {
            canAnswer = false;
            win.play();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gameover();
                }
            }, 2000);
        }
    }

    private void setupSet() {
        Map.Entry<String, SetType> entry = sets.getRandomEntry();
        text.setText(entry.getKey());
        image.setImageResource(sets.loadImage(this));
        answer = entry.getValue();
    }

    @Override
    public int getScore() {
        return !isGameOver() ? score : score > 0 ? score : 0;
    }

    @Override
    public int getView() {
        return R.layout.activity_sets;
    }

    @Override
    public Game getGame() {
        return Game.SETS;
    }

    @Override
    public void onCountdownTick(long remaining) {

    }

    @Override
    public void onCountdownFinish() {
        canAnswer = false;
        lose.play();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gameover();
            }
        }, 2000);
    }
}
