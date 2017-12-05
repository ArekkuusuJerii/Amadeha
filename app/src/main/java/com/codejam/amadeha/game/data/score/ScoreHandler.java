package com.codejam.amadeha.game.data.score;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.core.internal.deps.guava.collect.ImmutableMap;
import android.support.test.espresso.core.internal.deps.guava.collect.Lists;

import com.codejam.amadeha.game.core.JsonLoader;
import com.codejam.amadeha.game.data.GameInfo;
import com.codejam.amadeha.game.data.registry.Game;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

/**
 * This file was created by Snack on 23/02/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public final class ScoreHandler {

    private static final String SCORES = "scores";

    public static ImmutableMap<Game, List<Score>> getScores(Context context) {
        ImmutableMap.Builder<Game, List<Score>> builder = new ImmutableMap.Builder<>();

        for (Game game : Game.values()) {
            builder.put(game, getScores(context, game));
        }

        return builder.build();
    }

    public static List<Score> getScores(Context context, Game game) {
        SharedPreferences preferences = context.getSharedPreferences(SCORES, Context.MODE_PRIVATE);
        if(preferences.contains(game.toString())) {
            String json = preferences.getString(game.toString(), "");

            List<Score> list = new JsonLoader<>(new TypeToken<List<Score>>() {}, json).construct();
            Collections.sort(list);
            return list;
        }
        return Lists.newArrayList();
    }

    public static void saveScore(Context context, Game game, Score score) {
        SharedPreferences preferences = context.getSharedPreferences(SCORES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        List<Score> list = GameInfo.getScores().get(game);
        list.add(score);
        Collections.sort(list);

        editor.putString(game.toString(), new Gson().toJson(list.subList(0, Math.min(list.size(), 3))));
        editor.apply();
    }

    public static void saveScores(Context context, Game game, List<Score> list) {
        SharedPreferences preferences = context.getSharedPreferences(SCORES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Collections.sort(list);

        editor.putString(game.toString(), new Gson().toJson(list.subList(0, Math.min(list.size(), 3))));
        editor.apply();
    }

    public static void deleteScore(Context context, Game game) {
        SharedPreferences preferences = context.getSharedPreferences(SCORES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        GameInfo.getScores().get(game).clear();
        editor.remove(game.toString());
        editor.apply();
    }
}
