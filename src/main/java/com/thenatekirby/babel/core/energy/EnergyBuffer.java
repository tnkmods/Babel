package com.thenatekirby.babel.core.energy;

public class EnergyBuffer {
    final int capacity;
    final int maxReceive;
    final int maxExtract;
    final int current;

    public EnergyBuffer(int capacity, int maxReceive, int maxExtract, int current) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.current = current;
    }

    public boolean isValid() {
        return capacity > 0;
    }

    public static EnergyBuffer ZERO = new EnergyBuffer(0, 0, 0, 0);
}
