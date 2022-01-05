package com.thenatekirby.babel.machine.config;

// ====---------------------------------------------------------------------------====

public class ExperienceBuffer {
    public static ExperienceBuffer ZERO = new ExperienceBuffer(0, 0, 0, 0);

    public final int capacity;
    public final int maxReceive;
    public final int maxExtract;
    public final int current;

    public ExperienceBuffer(int capacity, int maxReceive, int maxExtract, int current) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.current = current;
    }
}
