package com.codejam.amadeha.game.core;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * This file was created by Snack on 06/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class JsonLoader<T> {

    private TypeToken<T> responseType;
    private String jsonString;

    public JsonLoader(String jsonString) {
        this.jsonString = jsonString;
    }

    public JsonLoader(TypeToken<T> responseType, String jsonString) {
        this.responseType = responseType;
        this.jsonString = jsonString;
    }

    public T construct() {
        return new Gson().fromJson(jsonString, responseType.getType());
    }

    @Nullable
    public JSONArray getJsonArray() {
        try {
            return new JSONArray(jsonString);
        } catch (Exception e) {
            Log.e("[FileReader]", "Unhandled exception while using JsonLoader", e);
        }
        return null;
    }

    public static class ListWithElements<T> implements ParameterizedType {

        private Class<T> elementsClass;

        public ListWithElements(Class<T> elementsClass) {
            this.elementsClass = elementsClass;
        }

        public Type[] getActualTypeArguments() {
            return new Type[] {elementsClass};
        }

        public Type getRawType() {
            return List.class;
        }

        public Type getOwnerType() {
            return null;
        }
    }
}
