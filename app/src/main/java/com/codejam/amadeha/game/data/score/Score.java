package com.codejam.amadeha.game.data.score;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * This file was created by Snack on 06/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */
public class Score implements Comparable<Score> {

    public String user;
    public Date date;
    public int score;

    public Score(String user, Date date, int score) {
        this.user = user;
        this.date = date;
        this.score = score;
    }

    public Score(String user, int score) {
        this.date = new Date();
        this.user = user;
        this.score = score;
    }

    @Override
    public int compareTo(@NonNull Score o) {
        return o.score > score ? 1 : o.score < score ? -1 : 0;
    }
}
