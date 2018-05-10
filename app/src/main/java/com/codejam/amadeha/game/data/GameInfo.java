package com.codejam.amadeha.game.data;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.test.espresso.core.internal.deps.guava.collect.ImmutableList;
import android.support.test.espresso.core.internal.deps.guava.collect.ImmutableMap;
import android.view.Window;
import android.widget.VideoView;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.core.widget.DialogWrapper;
import com.codejam.amadeha.game.data.profile.User;
import com.codejam.amadeha.game.data.profile.UserHandler;
import com.codejam.amadeha.game.data.registry.Game;
import com.codejam.amadeha.game.data.score.Score;
import com.codejam.amadeha.game.data.score.ScoreHandler;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

/**
 * This file was created by Snack on 06/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public final class GameInfo {

    private static final User GUEST = new User(UUID.fromString("D1C10E2A-C4F9-11E7-ABC4-CEC278B6B50A"), "Guest");
    private static ImmutableMap<Game, List<Score>> scores;
    private static List<User> users;
    //The current user, null by default
    private static User user;

    public static void init(Context context) {
        scores = ScoreHandler.getScores(context);
        users = UserHandler.getUsers(context);
    }

    public static void addUser(Context context, String name) {
        User user = new User(UUID.randomUUID(), name);
        UserHandler.addUser(context, user);
        users.add(user);
    }

    public static void removeUser(Context context, User user) {
        UserHandler.removeUser(context, user.id.toString());
        users.remove(user);
    }

    public static void saveGame(Context context, Game game, int points) {
        Score score = new Score(getUser().name, points);
        ScoreHandler.saveScore(context, game, score);
        //Update User
        if (!isGuest()) {
            for (Game unblocked : Game.values()) {
                if (unblocked.canUnblock(game, score)) {
                    if (user.levels.add(unblocked)) {
                        user.levelUp = true;
                    }
                }
            }
            user.score = user.score + score.score;
            updateProgress(context, game);
            UserHandler.modifyUser(context, getUser());
        }
    }

    private static void updateProgress(Context context, Game game) {
        if (user.levels.containsAll(Arrays.asList(Game.values()))) {
            user.progress = null;
            user.levels.clear();
            user.wins++;
            //Show final video
            Dialog dialog = new DialogWrapper(context, R.layout.activity_final_screen)
                    .setFeature(Window.FEATURE_NO_TITLE)
                    .setCancelable(true)
                    .setMinimizable(true)
                    .build(0.9F, 0.9F);
            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.game_over);
            VideoView video = dialog.findViewById(R.id.video);
            video.setVideoURI(uri);
            video.start();
            dialog.show();
        } else {
            user.progress = game;
        }
    }

    public static ImmutableMap<Game, List<Score>> getScores() {
        return scores;
    }

    public static ImmutableList<User> getUsers() {
        return ImmutableList.copyOf(users);
    }

    public static User getUser() {
        return user != null ? user : GUEST;
    }

    public static void setUser(@Nullable User user) {
        GameInfo.user = user;
    }

    public static boolean isGuest() {
        return user == null;
    }
}
