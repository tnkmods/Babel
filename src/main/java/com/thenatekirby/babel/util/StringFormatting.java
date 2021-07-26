package com.thenatekirby.babel.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class StringFormatting {
    public static String formatNumber(long number) {
        return NumberFormat.getInstance().format(number);
    }

    public static String formatNumber(int number) {
        return formatNumber((long) number);
    }

    public static String formatNumber(double number) {
        return NumberFormat.getInstance().format(number);
    }

    public static String formatNumber(float number) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        return decimalFormat.format(number);
    }
}
