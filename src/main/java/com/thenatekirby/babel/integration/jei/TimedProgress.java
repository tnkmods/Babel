package com.thenatekirby.babel.integration.jei;

import com.thenatekirby.babel.core.progress.IProgress;
import mezz.jei.api.gui.ITickTimer;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class TimedProgress implements IProgress {
    private final ITickTimer tickTimer;

    public TimedProgress(@Nonnull ITickTimer tickTimer) {
        this.tickTimer = tickTimer;
    }

    @Override
    public int getProgressMin() {
        return 0;
    }

    @Override
    public int getProgressMax() {
        return tickTimer.getMaxValue();
    }

    @Override
    public int getProgressCurrent() {
        return tickTimer.getValue();
    }
}