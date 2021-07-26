package com.thenatekirby.babel.api;

import com.thenatekirby.babel.core.energy.EnergyStats;

import javax.annotation.Nonnull;

public interface IEnergyStatsProvider {
    @Nonnull
    EnergyStats getEnergyStats();
}
