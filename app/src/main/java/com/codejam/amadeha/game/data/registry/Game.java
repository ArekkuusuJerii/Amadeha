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

import java.util.Locale;

/**
 * This file was created by Snack on 08/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public enum Game {
    SETS(SetsScreen.class, Sets.class, R.string.sets_game, R.layout.fragment_sets, R.raw.sets) {
        @Override
        public boolean canUnblock(Game from, Score score) {
            return from == SETS && score.score >= 40;
        }
    },
    FUNCTION(FunctionScreen.class, Function.class, R.string.function_game, R.layout.fragment_function, R.raw.function) {
        @Override
        public boolean canUnblock(Game from, Score score) {
            return from == SETS && score.score >= 30;
        }
    },
    EQUATION(EquationScreen.class, Equation.class, R.string.equation_game, R.layout.fragment_equation, R.raw.equation) {
        @Override
        public boolean canUnblock(Game from, Score score) {
            return from == FUNCTION && score.score >= 30;
        }
    },
    MATRIX(MatrixScreen.class, Matrix.class, R.string.matrix_game, R.layout.fragment_matrix, R.raw.matrix) {
        @Override
        public boolean canUnblock(Game from, Score score) {
            return from == EQUATION && score.score >= 40;
        }
    },
    STATISTIC(StatisticScreen.class, Statistic.class, R.string.statistic_game, R.layout.fragment_statistic, R.raw.statistic) {
        @Override
        public boolean canUnblock(Game from, Score score) {
            return from == MATRIX && score.score >= 40;
        }
    };

    public final Class level;
    public final Class json;
    public final int name;
    public final int fragment;
    public final int resource;

    Game(Class level, Class json, int name, int fragment, int resource) {
        this.level = level;
        this.json = json;
        this.name = name;
        this.fragment = fragment;
        this.resource = resource;
    }

    public boolean canUnblock(Game from, Score score) {
        return true;
    }

    public boolean isUnblocked(User user) {
        return ordinal() == 0 || (user.levels != null && user.levels.contains(this));
    }

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }
}
