package com.codejam.amadeha.game;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.core.widget.ColumnListAdapter;
import com.codejam.amadeha.game.core.widget.DialogWrapper;
import com.codejam.amadeha.game.core.widget.SimpleAnimationListener;
import com.codejam.amadeha.game.core.widget.SimpleSeekBarListener;
import com.codejam.amadeha.game.core.widget.SimpleTouchListener;
import com.codejam.amadeha.game.data.GameInfo;
import com.codejam.amadeha.game.data.registry.Game;
import com.codejam.amadeha.game.data.score.Score;
import com.codejam.amadeha.game.data.score.ScoreHandler;
import com.codejam.amadeha.game.data.settings.MusicHelper;
import com.codejam.amadeha.game.data.settings.MusicHelper.SoundType;

import java.util.List;

/**
 * This file was created by Snack on 07/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class LevelsScreen extends AppCompatActivity {

    private ViewPager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
        LevelAdapter adapter = new LevelAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.container);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(Game.values().length);
        pager.addOnPageChangeListener(new LevelBlocking());
    }

    public void showSettings(View view) {
        SimpleSeekBarListener listener = new SimpleSeekBarListener() {

            private final MusicHelper.Sound drag = MusicHelper.load(getBaseContext(), SoundType.EFFECT, R.raw.card1);
            private final MusicHelper.Sound drop = MusicHelper.load(getBaseContext(), SoundType.EFFECT, R.raw.card2);

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SoundType type = seekBar.getId() == R.id.volume_music
                        ? SoundType.MUSIC : SoundType.EFFECT;

                MusicHelper.setVolume(getBaseContext(), type, (float) progress / (float) seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                MusicHelper.play(drag, 0);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MusicHelper.play(drop, 0);
            }
        };
        Dialog wrapper = new DialogWrapper(this, R.layout.activity_settings)
                .setStyle(R.style.transparentDialog)
                .setFeature(Window.FEATURE_NO_TITLE)
                .setMinimizable(true)
                .setCancelable(true)
                .build(0.75F, 0.65F);
        SeekBar music = wrapper.findViewById(R.id.volume_music);
        music.setProgress((int) (MusicHelper.getVolumes().get(SoundType.MUSIC) * (float) music.getMax()));
        music.setOnSeekBarChangeListener(listener);
        SeekBar effect = wrapper.findViewById(R.id.volume_effect);
        effect.setProgress((int) (MusicHelper.getVolumes().get(SoundType.EFFECT) * (float) effect.getMax()));
        effect.setOnSeekBarChangeListener(listener);
        wrapper.show();
    }

    public void showMaxScore() {
        Game game = getCurrentGame();
        List<Score> scores = GameInfo.getScores().get(game);
        String string = getString(R.string.score_undefined);

        if (scores != null && !scores.isEmpty()) {
            Score score = scores.get(0); //First Score
            string = getString(R.string.score) + ": " + score.getScore() + " " +
                    getString(R.string.score_date) + ": " + score.getDate() + " " +
                    getString(R.string.score_user) + ": " + score.getUser();
        }
        Snackbar.make(findViewById(R.id.icon_float_credits), string, Snackbar.LENGTH_INDEFINITE).show();
    }

    public void closeGame(View view) {
        MusicHelper.stopBackground(getBaseContext());
        finish();
    }

    public void play(View view) {
        startActivity(new Intent(getBaseContext(), getCurrentGame().getLevel()));
    }

    public void showScores(View view) {
        final Dialog wrapper = new DialogWrapper(this, R.layout.activity_score)
                .setStyle(R.style.transparentDialog)
                .setFeature(Window.FEATURE_NO_TITLE)
                .setMinimizable(true)
                .setCancelable(true)
                .build(1F, 1F);
        updateScores(wrapper);
    }

    private void updateScores(Dialog wrapper) {
        ColumnListAdapter.Builder adapter = new ColumnListAdapter.Builder(this)
                .addRow()
                .addColumn(inflateScoreRow(
                        getText(R.string.player).toString(),
                        getText(R.string.score).toString(),
                        getText(R.string.date).toString())
                ).close();

        List<Score> get = GameInfo.getScores().get(getCurrentGame());
        for (int i = 0; i < get.size() && i < 3; i++) {
            Score score = get.get(i);
            View row = inflateScoreRow(score.getUser(), String.valueOf(score.getScore()), score.getDate().toString());
            adapter.addRow().addColumn(row).close();
        }

        ((ListView) wrapper.findViewById(R.id.score_list)).setAdapter(adapter.build());
        wrapper.show();
    }

    private View inflateScoreRow(String user, String score, String date) {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams")
        View row = inflater.inflate(R.layout.score_row, null);
        ((TextView) row.findViewById(R.id.user)).setText(user);
        ((TextView) row.findViewById(R.id.score)).setText(score);
        ((TextView) row.findViewById(R.id.date)).setText(date);
        return row;
    }

    public void scroll(View view) {
        int scroll = view.getId() == R.id.arrow_right ? View.FOCUS_RIGHT : View.FOCUS_LEFT;
        pager.arrowScroll(scroll);
    }

    public Game getCurrentGame() {
        int position = pager.getCurrentItem();
        return Game.values()[position];
    }

    @Override
    protected void onPause() {
        MusicHelper.stopBackground(getBaseContext());
        super.onPause();
    }

    @Override
    protected void onResume() {
        MusicHelper.playBackground(getBaseContext(), R.raw.ambient_loop);
        showMaxScore();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MusicHelper.stopBackground(getBaseContext());
    }

    public static class LevelFragment extends Fragment {

        public static LevelFragment newInstance(int position) {
            LevelFragment fragment = new LevelFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            int frag = getArguments().getInt("position");
            Game game = Game.values()[frag];
            View view = inflater.inflate(game.getFragment(), container, false);
            View right = view.findViewById(R.id.arrow_right);
            View left = view.findViewById(R.id.arrow_left);
            View icon = view.findViewById(R.id._game);
            if (GameInfo.isGuest() || game.isUnblocked(GameInfo.getUser())) {
                View check = view.findViewById(R.id.screen_check);
                View image = view.findViewById(R.id.screen_block);
                check.setVisibility(View.INVISIBLE);
                image.setVisibility(View.INVISIBLE);
            } else {
                View play = view.findViewById(R.id.play_game);
                play.setClickable(false);
            }

            Animation flick = AnimationUtils.loadAnimation(getContext(), R.anim.fade);
            flick.setRepeatCount(Animation.INFINITE);
            flick.setRepeatMode(Animation.REVERSE);
            flick.setDuration(1500);
            if(right != null) right.startAnimation(flick);
            if(left != null) left.startAnimation(flick);

            if(icon != null) {
                Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_image);
                rotate.setRepeatCount(Animation.INFINITE);
                rotate.setRepeatMode(Animation.RESTART);
                rotate.setDuration(100000);
                icon.startAnimation(rotate);
            }

            return view;
        }
    }

    public static class LevelAdapter extends FragmentPagerAdapter {

        public LevelAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return LevelFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return Game.values().length;
        }
    }

    private class LevelBlocking extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            if(state == ViewPager.SCROLL_STATE_IDLE) {
                final View view = pager.getChildAt(pager.getCurrentItem());
                final View check = view.findViewById(R.id.screen_check);
                final View image = view.findViewById(R.id.screen_block);
                final View play = view.findViewById(R.id.play_game);

                if (GameInfo.isGuest() || getCurrentGame().isUnblocked(GameInfo.getUser())) {
                    if (image.isShown()) {
                        Animation block = block();
                        block.setAnimationListener(new SimpleAnimationListener() {
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                image.setVisibility(View.INVISIBLE);
                                check.setVisibility(View.INVISIBLE);
                            }
                        });
                        image.startAnimation(block);
                        play.setClickable(true);

                        check.startAnimation(unblock());
                    }
                } else if (!image.isShown()) {
                    Animation unblock = unblock();
                    unblock.setAnimationListener(new SimpleAnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                            image.setVisibility(View.VISIBLE);
                            check.setVisibility(View.VISIBLE);
                        }

                    });
                    image.startAnimation(unblock);
                    play.setClickable(false);

                    check.startAnimation(block());
                }
                showMaxScore();
            }
        }

        private Animation block() {
            Animation block = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade);
            block.setDuration(1000);
            return block;
        }

        private Animation unblock() {
            Animation unblock = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade);
            unblock.setRepeatMode(Animation.REVERSE);
            unblock.setDuration(1000);
            return unblock;
        }
    }
}
