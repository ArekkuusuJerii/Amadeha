package com.codejam.amadeha.game.data.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.core.internal.deps.guava.collect.Maps;

import com.codejam.amadeha.game.data.settings.MusicHelper.SoundType;

import java.util.Map;

/**
 * This file was created by Snack on 10/03/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public final class SettingsHandler {

    private static final String SETTINGS = "settings";

    public static void setVolume(Context context, SoundType type, float volume) {
        SharedPreferences preferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(type.toString(), volume);
        editor.apply();
    }

    public static Map<SoundType, Float> getVolumes(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        Map<SoundType, Float> map = Maps.newHashMap();
        for (SoundType type : SoundType.values()) {
            map.put(type, preferences.getFloat(type.toString(), 1F));
        }
        return map;
    }
}
