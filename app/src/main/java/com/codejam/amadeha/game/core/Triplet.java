package com.codejam.amadeha.game.core;

/**
 * This file was created by Snack on 24/11/2017. It's distributed as part of Amadeha.
 * Get the source code in GitHub: https://github.com/ArekkuusuJerii/Amadeha
 * Amadeha is open source, and is distributed under the MIT licence.
 */

public class Triplet<F, S, T> {

    public final F first;
    public final S second;
    public final T third;

    public Triplet(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <A, B, C> Triplet <A, B, C> create(A a, B b, C c) {
        return new Triplet<>(a, b, c);
    }
}
