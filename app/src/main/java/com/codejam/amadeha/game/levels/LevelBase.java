package com.codejam.amadeha.game.levels;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.core.intefaze.ITickable;
import com.codejam.amadeha.game.core.widget.DialogWrapper;
import com.codejam.amadeha.game.core.widget.SimpleTouchListener;
import com.codejam.amadeha.game.data.GameInfo;
import com.codejam.amadeha.game.data.registry.Game;
import com.codejam.amadeha.game.data.settings.MusicHelper;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This file was created by Snack on 07/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public abstract class LevelBase extends Activity {

    protected final Random rand = new Random();
    protected MusicHelper.Sound incorrect;
    protected MusicHelper.Sound correct;
    protected MusicHelper.Sound lose;
    protected MusicHelper.Sound win;
    protected MusicHelper.Sound tick;
    protected long countDown = 0L;
    protected int level;
    private CountDownTimer timer;
    private boolean gameOver;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(getView());
        //Issue game start
        final Dialog wrapper = new DialogWrapper(this, R.layout.activity_game_prompt)
                .setFeature(Window.FEATURE_NO_TITLE)
                .build(0.85F, 0.75F);
        wrapper.findViewById(R.id.instruction_play).setOnTouchListener(new SimpleTouchListener() {
            @Override
            public void touchLift(View v) {
                init();
                iterate();
                wrapper.cancel();
            }
        });
        wrapper.findViewById(R.id.instruction_exit).setOnTouchListener(new SimpleTouchListener() {
            @Override
            public void touchLift(View v) {
                finish();
                wrapper.cancel();
            }
        });
        ((ImageView) wrapper.findViewById(R.id.instructor)).setImageResource(GameInfo.getUser().character.img);
        ((TextView) wrapper.findViewById(R.id.string)).setText(getInstruction());
        wrapper.show();
        incorrect = MusicHelper.load(getBaseContext(), MusicHelper.SoundType.EFFECT, R.raw.incorrect);
        correct = MusicHelper.load(getBaseContext(), MusicHelper.SoundType.EFFECT, R.raw.correct);
        lose = MusicHelper.load(getBaseContext(), MusicHelper.SoundType.EFFECT, R.raw.lose);
        win = MusicHelper.load(getBaseContext(), MusicHelper.SoundType.EFFECT, R.raw.win);
        tick = MusicHelper.load(getBaseContext(), MusicHelper.SoundType.EFFECT, R.raw.tick);
        showInstructions();
    }

    public abstract int getInstruction();

    public void showInstructions() {
        //For rent
    }

    public void skip(View view) {
        //For rent
    }

    public final void startCountdown(long milliseconds) {
        stopCountdown();
        timer = new CountDownTimer(milliseconds, 1000) {

            private final TextView text = (TextView) findViewById(R.id.countdown);

            @Override
            public void onTick(long remaining) {
                text.setText(String.format(getText(R.string.chronometer_placeholder).toString()
                        , TimeUnit.MILLISECONDS.toMinutes(remaining) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(remaining))
                        , TimeUnit.MILLISECONDS.toSeconds(remaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remaining))
                ));
                countDown = remaining;
                updateBarProgress();
                updateScore();
                tick.play();
                if (LevelBase.this instanceof ITickable) {
                    ((ITickable) LevelBase.this).onCountdownTick(remaining);
                }
            }

            @Override
            public void onFinish() {
                text.setText(String.format(getText(R.string.chronometer_placeholder).toString(), 0, 0));
                if(LevelBase.this instanceof ITickable) {
                    ((ITickable) LevelBase.this).onCountdownFinish();
                }
            }
        };
        timer.start();
    }

    public final void stopCountdown() {
        if(timer != null) {
            timer.cancel();
        }
    }

    public final void iterate() {
        play();
        updateBarProgress();
        updateScore();
        if(isGameOver()) {
            gameover();
        }
    }

    public final void updateBarProgress() {
        ProgressBar bar = findViewById(R.id.progress_bar);
        bar.setProgress(level);
    }

    public final void updateScore() {
        TextView text = findViewById(R.id.points);
        text.setText(String.valueOf(getScore()));
    }

    @Override
    protected final void onPause() {
        MusicHelper.stopBackground(getBaseContext());
        if(!isGameOver() && !isFinishing() && !isDestroyed()) pause();
        super.onPause();
    }

    @Override
    protected final void onResume() {
        MusicHelper.playBackground(getBaseContext(), R.raw.ambient_loop);
        super.onResume();
    }

    @Override
    public final void onBackPressed() {
        if(!isGameOver() && !isFinishing() && !isDestroyed()) pause();
    }

    public boolean canResume() {
        return false;
    }

    public final void pause() {
        if(canResume()) stopCountdown();
        final Dialog wrapper = new DialogWrapper(LevelBase.this, R.layout.activity_game_pause)
                .setMinimizable(!canResume())
                .setCancelable(!canResume())
                .setFeature(Window.FEATURE_NO_TITLE)
                .build(0.45F, 0.35F);
        wrapper.findViewById(R.id.pause_exit).setOnTouchListener(new SimpleTouchListener() {
            @Override
            public void touchLift(View v) {
                wrapper.cancel();
                finish();
            }
        });
        ((Button) wrapper.findViewById(R.id.pause_option)).setText(getText(canResume() ? R.string.continue_game : R.string.retry));
        wrapper.findViewById(R.id.pause_option).setOnTouchListener(new SimpleTouchListener() {
            @Override
            public void touchLift(View v) {
                if (canResume()) {
                    startCountdown(countDown);
                    wrapper.cancel();
                } else {
                    stopCountdown();
                    wrapper.cancel();
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        wrapper.show();
    }

    @Override
    protected void onDestroy() {
        stopCountdown();
        super.onDestroy();
    }

    public final void gameover() {
        setGameOver();
        stopCountdown();
        if(getScore() > 0) {
            GameInfo.save(LevelBase.this, getGame(), getScore());
        }
        final Dialog wrapper = new DialogWrapper(LevelBase.this, R.layout.activity_game_over)
                .setFeature(Window.FEATURE_NO_TITLE)
                .build(0.75F, 0.5F);
        wrapper.findViewById(R.id.over_replay).setOnTouchListener(new SimpleTouchListener() {
            @Override
            public void touchLift(View v) {
                finish();
                startActivity(getIntent());
                wrapper.cancel();
            }
        });
        wrapper.findViewById(R.id.over_exit).setOnTouchListener(new SimpleTouchListener() {
            @Override
            public void touchLift(View v) {
                finish();
                wrapper.cancel();
            }
        });
        ((TextView) wrapper.findViewById(R.id.over_score)).setText(String.format(getText(R.string.score_final).toString(), getScore()));
        wrapper.show();
    }

    public final boolean isGameOver() {
        return gameOver;
    }

    public final void setGameOver() {
        this.gameOver = true;
    }

    public abstract void init();

    public abstract void play();

    public abstract int getScore();

    public abstract int getView();

    public abstract Game getGame();
}
