package com.thenatekirby.babel.core.capability;

// ====---------------------------------------------------------------------------====

public class ExperienceBuffer {
    public static ExperienceBuffer ZERO = new ExperienceBuffer(0, 0, 0, 0);

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
}
