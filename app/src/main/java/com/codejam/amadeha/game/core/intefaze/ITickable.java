package com.codejam.amadeha.game.core.intefaze;

/**
 * This file was created by Snack on 28/02/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */
public interface ITickable {

    void onCountdownTick(long remaining);

    void onCountdownFinish();
}
