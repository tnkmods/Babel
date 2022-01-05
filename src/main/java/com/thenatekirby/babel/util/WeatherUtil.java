package com.thenatekirby.babel.util;

import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class WeatherUtil {

    // region Manipulation

    private static void setWeather(@Nonnull ServerLevel level, int clearTime, int rainTime, boolean isRaining, boolean isStorming) {
        level.setWeatherParameters(clearTime, rainTime, isRaining, isStorming);
    }

    public static void setClear(@Nonnull ServerLevel level) {
        setWeather(level, 6000, 0, false, false);
    }

    public static void setClear(@Nonnull ServerLevel level, int duration) {
        setWeather(level, duration, 0, false, false);
    }

    public static void setRaining(@Nonnull ServerLevel level) {
        setWeather(level, 0, 6000, true, false);
    }

    public static void setRaining(@Nonnull ServerLevel level, int duration) {
        setWeather(level, 0, duration, true, false);
    }

    public static void setStorming(@Nonnull ServerLevel level) {
        setWeather(level, 0, 6000, true, true);
    }

    public static void setStorming(@Nonnull ServerLevel level, int duration) {
        setWeather(level, 0, duration, true, true);
    }

    // endregion
}
