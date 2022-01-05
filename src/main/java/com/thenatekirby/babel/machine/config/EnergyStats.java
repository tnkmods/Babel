package com.thenatekirby.babel.machine.config;

// ====---------------------------------------------------------------------------====

import javax.annotation.Nonnull;

public class EnergyStats {
    private int accepts;
    private int consumes;
    private float efficiency;
    private float speed;

    public EnergyStats(int accepts, int consumes) {
        this.accepts = accepts;
        this.consumes = consumes;
        this.efficiency = 1.0f;
        this.speed = 1.0f;
    }

    @Nonnull
    public static EnergyStats make() {
        return new EnergyStats(0, 0);
    }

    public void setAccepts(int accepts) {
        this.accepts = accepts;
    }

    public int getAccepts() {
        return accepts;
    }

    public void setConsumes(int consumes) {
        this.consumes = consumes;
    }

    public int getConsumes() {
        return consumes;
    }

    public void setEfficiency(float efficiency) {
        this.efficiency = efficiency;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }
}
