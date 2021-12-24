package com.thenatekirby.babel.machine.config;

// ====---------------------------------------------------------------------------====

public class EnergyBuffer {
    public static EnergyBuffer ZERO = new EnergyBuffer(0, 0, 0, 0);

    public final int capacity;
    public final int maxReceive;
    public final int maxExtract;
    public final int current;

    public EnergyBuffer(int capacity, int maxReceive, int maxExtract, int current) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.current = current;
    }

    public boolean isValid() {
        return capacity > 0;
    }
}
