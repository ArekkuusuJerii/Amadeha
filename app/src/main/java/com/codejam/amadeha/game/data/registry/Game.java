package com.codejam.amadeha.game.data.registry;

import com.codejam.amadeha.R;
import com.codejam.amadeha.game.data.profile.User;
import com.codejam.amadeha.game.data.score.Score;
import com.codejam.amadeha.game.levels.equation.Equation;
import com.codejam.amadeha.game.levels.equation.EquationScreen;
import com.codejam.amadeha.game.levels.function.Function;
import com.codejam.amadeha.game.levels.function.FunctionScreen;
import com.codejam.amadeha.game.levels.matrix.Matrix;
import com.codejam.amadeha.game.levels.matrix.MatrixScreen;
import com.codejam.amadeha.game.levels.sets.Sets;
import com.codejam.amadeha.game.levels.sets.SetsScreen;
import com.codejam.amadeha.game.levels.statistic.Statistic;
import com.codejam.amadeha.game.levels.statistic.StatisticScreen;

/**
 * This file was created by Snack on 08/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public enum Game {
    EQUATION (EquationScreen.class,  Equation.class,  R.layout.fragment_equation,  R.raw.equation){
        @Override
        public boolean canUnblock(Game from, Score score) {
            return from == FUNCTION && score.getScore() >= 20;
        }
    },
    FUNCTION (FunctionScreen.class,  Function.class,  R.layout.fragment_function,  R.raw.function){
        @Override
        public boolean isUnblocked(User user) {
            return true;
        }
    },
    STATISTIC(StatisticScreen.class, Statistic.class, R.layout.fragment_statistic, R.raw.statistic){
        @Override
        public boolean canUnblock(Game from, Score score) {
            return from == FUNCTION && score.getScore() >= 20;
        }
    },
    SETS     (SetsScreen.class,      Sets.class,      R.layout.fragment_sets,      R.raw.sets){
        @Override
        public boolean canUnblock(Game from, Score score) {
            return from == FUNCTION && score.getScore() >= 20;
        }
    },
    MATRIX   (MatrixScreen.class,    Matrix.class,    R.layout.fragment_matrix,    R.raw.matrix){
        @Override
        public boolean isUnblocked(User user) {
            return true;
        }

        @Override
        public boolean canUnblock(Game from, Score score) {
            return from == FUNCTION && score.getScore() >= 20;
        }
    };

    private final Class level;
    private final Class json;
    private final int fragment;
    private final int resource;

    Game(Class level, Class json, int fragment, int resource) {
        this.level = level;
        this.json = json;
        this.fragment = fragment;
        this.resource = resource;
    }

    public boolean canUnblock(Game from, Score score) {
        return true;
    }

    public boolean isUnblocked(User user) {
        return user.getLevels() != null && user.getLevels().contains(this);
    }

    public Class getLevel() {
        return level;
    }

    public Class getJson() {
        return json;
    }

    public int getFragment() {
        return fragment;
    }

    public int getRaw() {
        return resource;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
