package com.thenatekirby.babel.core.api;

import com.thenatekirby.babel.machine.config.EnergyStats;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public interface IEnergyStatsProvider {
    @Nonnull
    EnergyStats getEnergyStats();
}
