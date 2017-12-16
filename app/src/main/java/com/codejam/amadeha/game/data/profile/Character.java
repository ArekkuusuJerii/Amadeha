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
    INSTRUCTOR_SPORT(R.drawable.instructor_deportes, 1),
    INSTRUCTOR_PANTSU(R.drawable.instructor_ropa_interior, 2),
    INSTRUCTOR_SANTA(R.drawable.instructor_santa_claus, 3);

    public final int img;
    public final int wins;

    Character(int img, int wins) {
        this.img = img;
        this.wins = wins;
    }

    public boolean canUnlock(User user) {
        return GameInfo.isGuest() || user.wins >= wins;
    }
}
