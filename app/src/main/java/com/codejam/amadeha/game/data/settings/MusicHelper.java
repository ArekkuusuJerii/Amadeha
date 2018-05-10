package com.codejam.amadeha.game.data.settings;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.test.espresso.core.internal.deps.guava.collect.ImmutableMap;
import android.support.test.espresso.core.internal.deps.guava.collect.Maps;

import java.util.Locale;
import java.util.Map;

/**
 * This file was created by Snack on 10/03/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public final class MusicHelper {

    private static final Map<SoundType, Float> volumes = Maps.newHashMap();
    private static SoundPool pool;
    private static Intent player;

    public static void init(Context context) {
        pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        volumes.putAll(SettingsHandler.getVolumes(context));
    }

    public static void setVolume(Context context, SoundType type, float volume) {
        float oldVolume = volumes.get(type);
        if(oldVolume != volume) {
            SettingsHandler.setVolume(context, type, volume);
            volumes.put(type, volume);
            if(type == SoundType.MUSIC && player != null) {
                player.putExtra("volume", volumes.get(SoundType.MUSIC));
                context.startService(player);
            }
        }
    }

    public static Sound load(Context context, SoundType type, int raw) {
        return load(context, type, raw, 0, 0);
    }

    public static Sound load(Context context, SoundType type, int raw, int priority, float rate) {
        int id = pool.load(context, raw, priority);
        return new Sound(type, id, priority, rate);
    }

    public static void play(Sound sound, int repeat) {
        float volume = volumes.get(sound.type);
        pool.play(sound.id, volume, volume, sound.priority, repeat, sound.rate);
    }

    public static synchronized void playBackground(Context context, int raw) {
        stopBackground(context);
        player = new Intent(context, SoundService.class);
        player.putExtra("volume", volumes.get(SoundType.MUSIC));
        player.putExtra("raw", raw);
        context.startService(player);
    }

    public static void pauseBackground(Context context, boolean paused) {
        if(player != null) {
            player.putExtra("pause", paused);
            context.startActivity(player);
        }
    }

    public static synchronized void stopBackground(Context context) {
        if(player != null) {
            context.stopService(player);
        }
    }

    public static void pause(Sound sound) {
        pool.pause(sound.id);
    }

    public static void resume(Sound sound) {
        pool.resume(sound.id);
    }

    public static void stop(Sound sound) {
        pool.unload(sound.id);
    }

    public static SoundPool getSoundPool() {
        return pool;
    }

    public static ImmutableMap<SoundType, Float> getVolumes() {
        return ImmutableMap.copyOf(volumes);
    }

    public enum SoundType {
        MUSIC,
        EFFECT;

        @Override
        public String toString() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
