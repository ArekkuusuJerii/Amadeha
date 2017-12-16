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

    public transient UUID id;
    public Character character = Character.INSTRUCTOR;
    public Set<Game> levels;
    public String name;
    public int score;
    public Game progress;
    public int wins;

    public User(){}

    public User(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
