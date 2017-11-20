package com.codejam.amadeha.game.levels.equation;

import android.support.test.espresso.core.internal.deps.guava.collect.Lists;
import android.support.test.espresso.core.internal.deps.guava.collect.Maps;
import android.util.Pair;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.data.registry.LevelRegistry;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This file was created by Snack on 07/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */
public class Equation {

    @SerializedName("type")
    private Type type;
    private String equation;
    private String[][] answers;
    private int score;
    private int time;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }

    public String[][] getAnswers() {
        return answers;
    }

    public void setAnswers(String[][] answers) {
        this.answers = answers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<String> getMappedString() {
        List<String> splits = Lists.newArrayList(equation);
        for (String[] row : answers) {
            for (int i = 0, size = splits.size(), index; i < size; i++) {
                String split = splits.get(i);
                index = split.indexOf(row[0]);
                if (index != -1 && !split.equals(row[0])) {
                    splits.remove(i);
                    if (index > 0) {
                        Collections.addAll(splits, split.substring(0, index));
                    }
                    Collections.addAll(splits, split.substring(index, index + row[0].length()));
                    if (index + row[0].length() < split.length()) {
                        Collections.addAll(splits, split.substring(index + row[0].length(), split.length()));
                    }
                }
                size = splits.size();
            }
        }
        return splits;
    }

    public Map<String, Pair<String, String>> getAnswerMap() {
        Map<String, Pair<String, String>> map = Maps.newHashMap();
        for (String[] row : answers) {
            Pair<String, String> pair = new Pair<>(row[1], row[2]);
            map.put(row[0], pair);
        }
        return map;
    }

    public List<String> getRandomAnswers() {
        List<String> ans = Lists.newArrayList();
        int[][] ints = new int[answers.length][];
        for (int i = 0; i < ints.length; i++) {
            String answer = answers[i][2];
            if (answer.contains("/")) {
                String[] split = answer.split("/");
                ints[i] = new int[]{Integer.valueOf(split[0]), Integer.valueOf(split[1])};
            } else {
                ints[i] = new int[]{Integer.valueOf(answer)};
            }
            if(!ans.contains(answer)) {
                ans.add(answer);
            }
        }
        for (int i = ans.size(); i < 5; i++) {
            int[] numbers = ints[LevelRegistry.RAND.nextInt(ints.length)];
            if (numbers.length == 1) {
                String answer = String.valueOf(numbers[0] + randInt());
                ans.add(answer);
            } else {
                String one = String.valueOf(numbers[0] + randInt());
                String two = String.valueOf(numbers[1] + randInt());
                ans.add(one + "/" + two);
            }
        }
        Collections.shuffle(ans);
        return ans;
    }

    private static int randInt() {
        return LevelRegistry.RAND.nextInt(6) + 1;
    }

    public enum Type {
        @SerializedName("0")
        FIRST_GRADE(R.string.equation_first_grade),
        @SerializedName("1")
        SECOND_GRADE(R.string.equation_second_grade),
        @SerializedName("2")
        BI_UNKNOWN(R.string.equation_bi_unknown);

        public final int name;

        Type(int name) {
            this.name = name;
        }
    }
}
