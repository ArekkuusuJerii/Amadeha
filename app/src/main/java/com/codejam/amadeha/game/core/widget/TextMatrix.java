package com.codejam.amadeha.game.core.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * This file was created by Snack on 04/04/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class TextMatrix extends AutoResizeTextView {

    private String[][] matrixArray;

    public TextMatrix(Context context) {
        super(context);
    }

    public TextMatrix(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextMatrix(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String[][] getMatrixArray() {
        return matrixArray;
    }

    public void setMatrixArray(String[][] matrixArray) {
        if(matrixArray != null) {
            this.matrixArray = matrixArray;
            StringBuilder builder = new StringBuilder();
            for (int i = 0, length = matrixArray.length; i < length; i++) {
                String[] row = matrixArray[i];
                for (int j = 0, rows = row.length; j < rows; j++) {
                    String c = row[j];
                    builder.append(c);
                    if (j != rows - 1) {
                        builder.append(" ");
                    }
                }
                if (i != length - 1) {
                    builder.append("\n");
                }
            }
            setMaxLines(matrixArray.length);
            setMinLines(matrixArray.length);
            setText(builder.toString());
        } else {
            this.matrixArray = null;
            setText("");
        }
    }
}
