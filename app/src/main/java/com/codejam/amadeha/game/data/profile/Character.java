package com.codejam.amadeha.game.data.profile;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.data.GameInfo;

/**
 * This file was created by Snack on 06/12/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public enum Character {
    INSTRUCTOR(R.drawable.instructor, 0),
    INSTRUCTOR_SPORT(R.drawable.instructor_deportes, 100),
    INSTRUCTOR_PANTSU(R.drawable.instructor_ropa_interior, 200),
    INSTRUCTOR_SANTA(R.drawable.instructor_santa_claus, 500);

    public final int img;
    public final int scoreUnlock;

    Character(int img, int scoreUnlock) {
        this.img = img;
        this.scoreUnlock = scoreUnlock;
    }

    public boolean canUnlock(User user) {
        return GameInfo.isGuest() || user.score >= scoreUnlock;
    }
}
