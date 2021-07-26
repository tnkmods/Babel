package com.thenatekirby.babel.util;

import com.thenatekirby.babel.core.WeatherType;
import com.thenatekirby.babel.core.tileentity.WorkingTileEntity;
import net.minecraft.command.impl.WeatherCommand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class WeatherUtil {
    // ====---------------------------------------------------------------------------====
    // region Info

    public static boolean isRaining(@Nonnull World world) {
        return world.getWorldInfo().isRaining();
    }

    public static boolean isStorming(@Nonnull World world) {
        return world.getWorldInfo().isThundering();
    }

    public static boolean isClear(@Nonnull World world) {
        return !isRaining(world) && !isStorming(world);
    }

    public static WeatherType getWeatherType(@Nonnull World world) {
        if (isClear(world)) {
            return WeatherType.CLEAR;
        } else if (isStorming(world)) {
            return WeatherType.STORMING;
        } else {
            return WeatherType.RAINING;
        }
    }

    public static boolean isWeatherType(@Nonnull World world, @Nonnull WeatherType weatherType) {
        return getWeatherType(world) == weatherType;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Manipulation

    public static void setClear(@Nonnull ServerWorld world) {
        world.func_241113_a_(6000, 0, false, false);
    }

    public static void setClear(@Nonnull ServerWorld world, int time) {
        world.func_241113_a_(time, 0, false, false);
    }

    public static void setRaining(@Nonnull ServerWorld world) {
        world.func_241113_a_(0, 6000, true, false);
    }

    public static void setRaining(@Nonnull ServerWorld world, int time) {
        world.func_241113_a_(0, time, true, false);
    }

    public static void setStorming(@Nonnull ServerWorld world) {
        world.func_241113_a_(0, 6000, true, true);
    }

    public static void setStorming(@Nonnull ServerWorld world, int time) {
        world.func_241113_a_(0, time, true, true);
    }

    // endregion
}
