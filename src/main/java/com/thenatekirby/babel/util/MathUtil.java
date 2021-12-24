package com.thenatekirby.babel.util;

public class MathUtil {
    public static int clamp(int amount, int min, int max) {
        if (amount < min) {
            return min;
        } else if (amount > max) {
            return max;
        }

        return amount;
    }
}
