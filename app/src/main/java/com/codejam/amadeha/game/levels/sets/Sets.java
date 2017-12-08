package com.codejam.amadeha.game.levels.sets;

import android.content.Context;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.data.registry.LevelRegistry;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This file was created by Snack on 07/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class Sets {

    public List<Entry> answers;
    public String image;

    public Entry getRandomEntry() {
        int index = LevelRegistry.RAND.nextInt(answers.size());
        return answers.get(index);
    }

    public int loadImage(Context context) {
        return context.getResources().getIdentifier(image, "drawable", context.getPackageName());
    }

    public static class Entry {
        public String l;
        public String r;
        public String q;
        public SetType answer;
        public int time;
    }

    public enum SetType {
        @SerializedName("none")
        NONE(R.drawable.set_none),
        @SerializedName("from")
        FROM(R.drawable.set_from),
        @SerializedName("empty")
        EMPTY(R.drawable.sets_empty),
        @SerializedName("union")
        UNION(R.drawable.set_union),
        @SerializedName("intersection")
        INTERSECTION(R.drawable.set_intersection);

        public final int image;

        SetType(int image) {
            this.image = image;
        }
    }
}
