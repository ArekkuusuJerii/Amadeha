package com.codejam.amadeha.game.levels.function;

import android.content.Context;
import android.content.res.Resources;

/**
 * This file was created by Snack on 07/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class Function {

    private String card_left;
    private String card_right;

    public String getCard_left() {
        return card_left;
    }

    public void setCard_left(String card_left) {
        this.card_left = card_left;
    }

    public String getCard_right() {
        return card_right;
    }

    public void setCard_right(String card_right) {
        this.card_right = card_right;
    }

    public Card[] getCards(Context context) {
        Resources resources = context.getResources();

        Card card_left = new Card();
        card_left.image = resources.getIdentifier(this.card_left, "drawable", context.getPackageName());
        Card card_right = new Card();
        card_right.image = resources.getIdentifier(this.card_right, "drawable", context.getPackageName());

        card_right.mirror = card_left;
        card_left.mirror = card_right;
        return new Card[]{card_left, card_right};
    }

    public static class Card {

        public int image;
        public Card mirror;
    }
}
