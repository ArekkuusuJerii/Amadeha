package com.codejam.amadeha.game.data.settings;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.test.espresso.core.internal.deps.guava.collect.ImmutableMap;

import java.util.Map;

/**
 * This file was created by Snack on 10/03/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public final class MusicHelper {

    private static Map<SoundType, Float> volumes;
    private static SoundPool pool;
    private static Intent player;

    public static void init(Context context) {
        pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        volumes = SettingsHandler.getVolumes(context);
    }

    public static void setVolume(Context context, SoundType type, float volume) {
        float oldVolume = volumes.get(type);
        if(oldVolume != volume) {
            SettingsHandler.setVolume(context, type, volume);
            volumes.put(type, volume);
            if(type == SoundType.MUSIC && player != null) {
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
            return name().toLowerCase();
        }
    }

    public static class Sound {

        private final SoundType type;
        private final int id;
        private final int priority;
        private final float rate;

        Sound(SoundType type, int id, int priority, float rate) {
            this.type = type;
            this.id = id;
            this.priority = priority;
            this.rate = rate;
        }

        public void play() {
            MusicHelper.play(this, 0);
        }
    }

    public static class SoundService extends Service {

        private MediaPlayer media;
        private int music;

        public SoundService() {
            super();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int id) {
            float volume = volumes.get(SoundType.MUSIC);

            int music = intent.getIntExtra("raw", 0);
            if (media == null || music != this.music) {
                media = MediaPlayer.create(this, music);
                this.music = music;
            }
            media.setVolume(volume, volume);
            media.setLooping(true);
            if (intent.getBooleanExtra("pause", false) && media.isPlaying()) {
                media.pause();
            } else if (!media.isPlaying()) {
                media.start();
            }

            return super.onStartCommand(intent, flags, id);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onDestroy() {
            media.stop();
            media.release();
        }
    }
}
