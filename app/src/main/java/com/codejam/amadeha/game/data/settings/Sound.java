package com.codejam.amadeha.game.data.settings;

/**
 * This file was created by Snack on 25/01/2018. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class Sound {

    public final MusicHelper.SoundType type;
    public final int id;
    public final int priority;
    public final float rate;

    Sound(MusicHelper.SoundType type, int id, int priority, float rate) {
        this.type = type;
        this.id = id;
        this.priority = priority;
        this.rate = rate;
    }

    public void play() {
        MusicHelper.play(this, 0);
    }
}
