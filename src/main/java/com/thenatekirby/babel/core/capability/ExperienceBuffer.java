package com.thenatekirby.babel.core.capability;

import com.thenatekirby.babel.core.energy.EnergyBuffer;

public class ExperienceBuffer {
    final int capacity;
    final int maxReceive;
    final int maxExtract;
    final int current;

    public ExperienceBuffer(int capacity, int maxReceive, int maxExtract, int current) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.current = current;
    }

    public boolean isValid() {
        return capacity > 0;
    }

    public static ExperienceBuffer ZERO = new ExperienceBuffer(0, 0, 0, 0);
}
