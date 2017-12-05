package com.codejam.amadeha.game.data;

import android.content.Context;
import android.support.test.espresso.core.internal.deps.guava.collect.ImmutableList;
import android.support.test.espresso.core.internal.deps.guava.collect.ImmutableMap;

import com.codejam.amadeha.game.data.profile.User;
import com.codejam.amadeha.game.data.profile.UserHandler;
import com.codejam.amadeha.game.data.registry.Game;
import com.codejam.amadeha.game.data.score.Score;
import com.codejam.amadeha.game.data.score.ScoreHandler;

import java.util.HashSet;
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

    public static void save(Context context, Game game, int points) {
        Score score = new Score(getUser().name, points);
        ScoreHandler.saveScore(context, game, score);
        //Update User
        if (!isGuest()) {
            if (user.levels == null) {
                user.levels = new HashSet<>();
            }
            for (Game unblocked : Game.values()) {
                if (unblocked.canUnblock(game, score)) {
                    user.levels.add(unblocked);
                }
            }
            user.score = user.score + score.score;
            UserHandler.modifyUser(context, getUser());
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
