package com.codejam.amadeha.game.levels.matrix;

/**
 * This file was created by Snack on 07/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */
public class Matrix {

    private String operation;
    private int[][] x;
    private int[][] y;
    private int[][] answer;

    public Matrix(int[][] x, int[][] y, String operation, int[][] answer) {
        this.x = x;
        this.y = y;
        this.operation = operation;
        this.answer = answer;
    }

    public int[][] getX() {
        return x;
    }

    public void setX(int[][] x) {
        this.x = x;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int[][] getY() {
        return y;
    }

    public void setY(int[][] y) {
        this.y = y;
    }

    public int[][] getAnswer() {
        return answer;
    }

    public void setAnswer(int[][] answer) {
        this.answer = answer;
    }

    public boolean isEqual(int[][] ans) {
        boolean equals = true;

        if (ans.length == answer.length) {
            for (int i = 0; i < ans.length; i++) {
                int[] row = ans[i];
                if (row.length == answer[i].length) {
                    for (int j = 0; j < row.length; j++) {
                        int c = row[j];
                        if (c != answer[i][j]) {
                            equals = false;
                            break;
                        }
                    }
                } else {
                    equals = false;
                    break;
                }
            }
        } else {
            equals = false;
        }

        return equals;
    }
}
