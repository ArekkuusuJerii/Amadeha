package com.codejam.amadeha.game;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.core.internal.deps.guava.collect.Lists;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.core.widget.ColumnListAdapter;
import com.codejam.amadeha.game.core.widget.DialogWrapper;
import com.codejam.amadeha.game.core.widget.SimpleAnimationListener;
import com.codejam.amadeha.game.core.widget.SimpleSeekBarListener;
import com.codejam.amadeha.game.core.widget.SimpleTouchListener;
import com.codejam.amadeha.game.data.GameInfo;
import com.codejam.amadeha.game.data.profile.Character;
import com.codejam.amadeha.game.data.profile.User;
import com.codejam.amadeha.game.data.profile.UserHandler;
import com.codejam.amadeha.game.data.registry.Game;
import com.codejam.amadeha.game.data.score.Score;
import com.codejam.amadeha.game.data.settings.MusicHelper;
import com.codejam.amadeha.game.data.settings.MusicHelper.SoundType;
import com.codejam.amadeha.game.levels.LevelBase;

import java.util.List;

/**
 * This file was created by Snack on 07/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public final class LevelsScreen extends AppCompatActivity {

    private Dialog settings;
    private Dialog highscores;
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
        if(settings != null && settings.isShowing()) return;
        if(settings == null) {
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

            settings = new DialogWrapper(this, R.layout.activity_settings)
                    .setStyle(R.style.transparentDialog)
                    .setFeature(Window.FEATURE_NO_TITLE)
                    .setMinimizable(true)
                    .setCancelable(true)
                    .build(1F, 1F);
            SeekBar music = settings.findViewById(R.id.volume_music);
            music.setProgress((int) (MusicHelper.getVolumes().get(SoundType.MUSIC) * (float) music.getMax()));
            music.setOnSeekBarChangeListener(listener);
            SeekBar effect = settings.findViewById(R.id.volume_effect);
            effect.setProgress((int) (MusicHelper.getVolumes().get(SoundType.EFFECT) * (float) effect.getMax()));
            effect.setOnSeekBarChangeListener(listener);
            //Characters
            ((TextView) settings.findViewById(R.id.score)).setText(String.valueOf(GameInfo.isGuest() ? " ∞ " : GameInfo.getUser().score));
            List<ImageView> instructors = Lists.newArrayList(
                    (ImageView) settings.findViewById(R.id.i_0),
                    (ImageView) settings.findViewById(R.id.i_1),
                    (ImageView) settings.findViewById(R.id.i_2),
                    (ImageView) settings.findViewById(R.id.i_3)
            );
            CharacterSelector selector = new CharacterSelector(instructors);
            int i = 0;
            for (View instructor : instructors) {
                Character character = Character.values()[i++];
                if (GameInfo.getUser().character == character) {
                    instructor.setBackgroundColor(getResources().getColor(R.color.dark_transparent));
                } else {
                    ((ImageView) instructor).setColorFilter(getResources().getColor(R.color.fontBlack));
                }
                instructor.setTag(character);
                instructor.setOnTouchListener(selector);
            }
        }

        settings.show();
    }

    private class CharacterSelector extends SimpleTouchListener {

        List<ImageView> instructors;

        CharacterSelector(List<ImageView> instructors) {
            this.instructors = instructors;
        }

        @Override
        public void touchLift(View instructor) {
            Character character = (Character) instructor.getTag();
            User user = GameInfo.getUser();
            if (character.canUnlock(user)) {
                user.character = character;
                if (!GameInfo.isGuest()) {
                    UserHandler.modifyUser(getBaseContext(), user);
                }
                instructor.setBackgroundColor(getResources().getColor(R.color.dark_transparent));
                ((ImageView) instructor).setColorFilter(0);
                for (View lock : instructors) {
                    if (lock != instructor) {
                        ((ImageView) lock).setColorFilter(getResources().getColor(R.color.fontBlack));
                        lock.setBackgroundColor(0);
                    }
                }
            } else {
                Toast.makeText(getBaseContext(), String.format(getString(R.string.needed_points), String.valueOf(character.wins)), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showMaxScore() {
        Game game = getCurrentGame();
        if(GameInfo.getScores() == null) return;
        List<Score> scores = GameInfo.getScores().get(game);
        String string = getString(R.string.score_undefined);
        if (scores != null && !scores.isEmpty()) {
            Score score = scores.get(0); //First Score
            string = getString(R.string.score_user) + ": " + score.user + " | " +
                    getString(R.string.score) + ": " + score.score + " | " +
                    getString(R.string.score_date) + ": " + score.date;
        }
        Snackbar.make(findViewById(R.id.icon_float_credits), string, Snackbar.LENGTH_INDEFINITE).show();
    }

    public void closeGame(View view) {
        MusicHelper.stopBackground(getBaseContext());
        finish();
    }

    public void play(View view) {
        Intent intent = new Intent(getBaseContext(), getCurrentGame().level);
        intent.putExtra(LevelBase.SHOW_INSTRUCTIONS, true);
        startActivity(intent);
    }

    public void showScores(View view) {
        if(highscores != null && highscores.isShowing()) return;
        if(highscores == null) {
            highscores = new DialogWrapper(this, R.layout.activity_score)
                    .setStyle(R.style.transparentDialog)
                    .setFeature(Window.FEATURE_NO_TITLE)
                    .setMinimizable(true)
                    .setCancelable(true)
                    .build(1F, 1F);
        }
        updateScores();
    }

    private void updateScores() {
        if(highscores != null && highscores.isShowing()) return;
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
            View row = inflateScoreRow(score.user, String.valueOf(score.score), score.date.toString());
            adapter.addRow().addColumn(row).close();
        }

        ((ListView) highscores.findViewById(R.id.score_list)).setAdapter(adapter.build());
        highscores.show();
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
        public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
            int frag = getArguments().getInt("position");
            Game game = Game.values()[frag];
            View view = inflater.inflate(game.fragment, container, false);
            Typeface type = Typeface.createFromAsset(container.getContext().getAssets(), "fonts/Orbitron-Regular.ttf");
            ((TextView) view.findViewById(R.id.game_title)).setTypeface(type);
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
