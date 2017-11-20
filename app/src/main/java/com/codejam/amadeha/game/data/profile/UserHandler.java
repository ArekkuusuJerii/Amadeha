package com.codejam.amadeha.game.data.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.core.internal.deps.guava.collect.Lists;

import com.google.gson.Gson;

import java.util.List;
import java.util.UUID;

/**
 * This file was created by Snack on 20/02/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public final class UserHandler {

    private static final String PROFILES = "profiles";

    public static List<User> getUsers(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PROFILES, Context.MODE_PRIVATE);
        List<User> list = Lists.newArrayList();

        for (String uuid : preferences.getAll().keySet()) {
            if(preferences.contains(uuid)) {
                String json = preferences.getString(uuid, "");

                User user = new Gson().fromJson(json, User.class);
                user.setId(UUID.fromString(uuid));
                list.add(user);
            }
        }

        return list;
    }

    public static void addUser(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences(PROFILES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(UUID.randomUUID().toString(), new Gson().toJson(user));
        editor.apply();
    }

    public static void modifyUser(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences(PROFILES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(user.getId().toString(), new Gson().toJson(user));
        editor.apply();
    }

    public static void removeUser(Context context, String uuid) {
        SharedPreferences preferences = context.getSharedPreferences(PROFILES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(uuid);
        editor.apply();
    }
}
