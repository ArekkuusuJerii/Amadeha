package com.codejam.amadeha.game.core;

import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * This file was created by Snack on 28/01/2017. It's distributed as part of AMADHEA.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/AMADHEA
 * AMADHEA is open source, and is distributed under the MIT licence.
 */

public class FileReader {

    public static String readFile(Resources resources, int id) {
        InputStream inputStream = resources.openRawResource(id);
        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
        }
        catch (Exception e) {
            Log.e("[FileReader]", "Unhandled exception while using FileReader", e);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                Log.e("[FileReader]", "Unhandled exception while using FileReader", e);
            }
        }

        return writer.toString();
    }
}
