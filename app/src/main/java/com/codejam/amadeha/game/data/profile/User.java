package com.codejam.amadeha.game.data.profile;

import com.codejam.amadeha.game.data.registry.Game;

import java.util.Set;
import java.util.UUID;

/**
 * This file was created by Snack on 06/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class User {

    private transient UUID id;
    private Set<Game> levels;
    private String name;
    private int score;

    public User(){}

    public User(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Set<Game> getLevels() {
        return levels;
    }

    public void setLevels(Set<Game> levels) {
        this.levels = levels;
    }

}
