package com.codejam.amadeha.game.levels.sets;

import android.content.Context;

import com.codejam.amadeha.game.data.registry.LevelRegistry;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * This file was created by Snack on 07/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class Sets {

    public Map<String, SetType> answers;
    public String image;

    public Map.Entry<String, SetType> getRandomEntry() {
        int index = LevelRegistry.RAND.nextInt(answers.size());
        int i = 0;

        Map.Entry<String, SetType> entry = null;
        for (Map.Entry<String, SetType> s : answers.entrySet()) {
            entry = s;
            if (i++ >= index) break;
        }
        return entry;
    }

    public int loadImage(Context context) {
        return context.getResources().getIdentifier(image, "drawable", context.getPackageName());
    }

    public enum SetType {
        @SerializedName("none")
        NONE(0, 0),
        @SerializedName("empty")
        EMPTY(0, 0),
        @SerializedName("union")
        UNION(0, 0),
        @SerializedName("intersection")
        INTERSECTION(0, 0);

        public final int name;
        public final int image;

        SetType(int name, int image) {
            this.name = name;
            this.image = image;
        }
    }
}
