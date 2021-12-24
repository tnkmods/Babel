package com.thenatekirby.babel.core.progress;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class Progress implements IProgress, INBTSerializable<CompoundTag> {
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

    @Nonnull
    public static Progress make() {
        return new Progress();
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

    public void advanceBy(int amount) {
        this.current += amount;
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

    public void clear() {
        reset();
        setMax(0);
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
        return Math.max(0, Math.min(current, max));
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region INBTSerializable

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("max", max);
        nbt.putInt("current", current);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.max = nbt.getInt("max");
        this.current = nbt.getInt("current");
    }

    // endregion
}
