package com.codejam.amadeha.game.data.settings;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * This file was created by Snack on 25/01/2018. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class SoundService extends Service {

    private MediaPlayer media;
    private int music;

    public SoundService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int id) {
        float volume = intent.getFloatExtra("volume", 0);
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
