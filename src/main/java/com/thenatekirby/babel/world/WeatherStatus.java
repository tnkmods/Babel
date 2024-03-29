package com.thenatekirby.babel.world;

import net.minecraft.world.level.Level;
import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

@SuppressWarnings("ClassCanBeRecord")
public class WeatherStatus {
    private final Level level;

    private WeatherStatus(@Nonnull Level level) {
        this.level = level;
    }

    // ====---------------------------------------------------------------------------====
    // region Factories

    public static WeatherStatus of(@Nonnull Level level) {
        return new WeatherStatus(level);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region info

    public boolean isRaining() {
        return level.isRaining();
    }

    public boolean isStorming() {
        return level.isThundering();
    }

    public boolean isClear() {
        return !isRaining() && !isStorming();
    }

    // endregion
}
