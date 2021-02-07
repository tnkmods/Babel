package com.thenatekirby.babel.core;

import com.thenatekirby.babel.api.IProgress;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

// ====---------------------------------------------------------------------------====

public class Progress implements IProgress, INBTSerializable<CompoundNBT> {
    public static final Progress NO_PROGRESS = new Progress();

    private int min = 0;
    private int max = 0;
    private int current = 0;

    // ====---------------------------------------------------------------------------====

    public Progress() {
        this(0, 0, 0);
    }

    public Progress(int min, int max, int current) {
        this.min = min;
        this.max = max;
        this.current = current;
    }

    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    public void setCurrent(int current) {
        this.current = current;
    }

    public void setMax(int max) {
        this.max = max;
    }


    // endregion
    // ====---------------------------------------------------------------------------====
    // region Helpers

    public void advance() {
        this.current += 1;
    }

    public boolean isComplete() {
        return this.current >= this.max;
    }

    public boolean canAdvance() {
        return this.max > 0;
    }

    public void reset() {
        this.current = 0;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region IProgress

    @Override
    public int getProgressMin() {
        return min;
    }

    @Override
    public int getProgressMax() {
        return max;
    }

    @Override
    public int getProgressCurrent() {
        return current;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region INBTSerializable

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("max", max);
        nbt.putInt("current", current);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.max = nbt.getInt("max");
        this.current = nbt.getInt("current");
    }

    // endregion
}
