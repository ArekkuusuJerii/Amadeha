package com.codejam.amadeha.game.data.registry;

import android.content.res.Resources;
import android.support.test.espresso.core.internal.deps.guava.collect.ImmutableList;
import android.support.test.espresso.core.internal.deps.guava.collect.ImmutableMap;
import android.support.test.espresso.core.internal.deps.guava.collect.Lists;

import com.codejam.amadeha.game.core.FileReader;
import com.codejam.amadeha.game.core.JsonLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This file was created by Snack on 06/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */
public class LevelRegistry {

    public static final Random RAND = new Random();
    public static ImmutableMap<Game, Level> levels;

    public static void init(Resources resources) {
        ImmutableMap.Builder<Game, Level> builder = new ImmutableMap.Builder<>();
        for (Game game : Game.values()) {
            builder.put(game, LevelRegistry.load(resources, game.getJson(), game.getRaw()));
        }
        levels = builder.build();
    }

    private static <T> Level<T> load(Resources resources, Class<T> t, int raw) {
        String json = FileReader.readFile(resources, raw);
        List<T> list = new Gson().fromJson(json , new JsonLoader.ListWithElements<>(t));
        return new Level<>(list);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getShuffledRegistry(Game game) {
        return (List<T>) levels.get(game).shuffle();
    }

    public static class Level<T> {
        private final List<T> list;

        Level(List<T> list) {
            this.list = list;
        }

        public ImmutableList<T> getList() {
            return ImmutableList.copyOf(list);
        }

        public List<T> shuffle() {
            List<T> shuffled = Lists.newArrayList(list);
            Collections.shuffle(shuffled, RAND);
            return shuffled;
        }

        public T random() {
            int index = RAND.nextInt(list.size());
            return list.get(index);
        }
    }

}
