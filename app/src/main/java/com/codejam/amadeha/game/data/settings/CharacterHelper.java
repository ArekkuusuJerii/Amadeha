package com.codejam.amadeha.game.data.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.codejam.amadeha.game.data.GameInfo;
import com.codejam.amadeha.game.data.profile.User;

/**
 * This file was created by Snack on 29/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public final class CharacterHelper {

    private static final String CHARACTER = "character";

    public static void setCharacter(Context context, Character character) {
        SharedPreferences preferences = context.getSharedPreferences(CHARACTER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        User user = GameInfo.getUser();
        editor.putString(user.id.toString(), character.name());
        editor.apply();
    }

    public static Character getCharacter(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(CHARACTER, Context.MODE_PRIVATE);
        User user = GameInfo.getUser();
        if(preferences.contains(user.id.toString())) {
            return Character.valueOf(preferences.getString(user.id.toString(), Character.DEFAULT.name()));
        } else {
            return Character.DEFAULT;
        }
    }

    public enum Character {
        DEFAULT
    }
}
