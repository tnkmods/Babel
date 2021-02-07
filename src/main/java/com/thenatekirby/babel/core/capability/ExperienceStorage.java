package com.thenatekirby.babel.core.capability;

import com.thenatekirby.babel.api.IExperienceStorage;
import com.thenatekirby.babel.api.IProgress;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

public class ExperienceStorage implements IExperienceStorage, INBTSerializable<CompoundNBT>, IProgress {
    private int capacity;
    private int current;
    private int maxExtract;
    private int maxReceive;
    private IExperienceStorageChangedObserver observer;

    public ExperienceStorage(int capacity, int current, int maxExtract, int maxReceive) {
        this.capacity = capacity;
        this.current = current;
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
    }

    public ExperienceStorage(@Nonnull ExperienceBuffer buffer) {
        this.capacity = buffer.capacity;
        this.current = buffer.current;
        this.maxExtract = buffer.maxExtract;
        this.maxReceive = buffer.maxReceive;
    }

    private void onExperienceChange() {
        if (this.observer != null) {
            this.observer.onExperienceChanged(this);
        }
    }

    public void setExperience(int current) {
        this.current = current;
    }

    public int getMaxExtract() {
        return maxExtract;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public int getCapacity() {
        return capacity;
    }

    // ====---------------------------------------------------------------------------====
    // region IExperienceStorage

    @Override
    public int receiveExperience(int experience, boolean simulate) {
        int received = Math.min(capacity - current, Math.min(experience, this.maxReceive));
        if (!simulate) {
            this.current += received;
            onExperienceChange();
        }

        return received;
    }

    @Override
    public int extractExperience(int experience, boolean simulate) {
        int extracted = Math.min(current, Math.min(experience, this.maxExtract));
        if (!simulate) {
            this.current -= extracted;
            onExperienceChange();
        }

        return extracted;
    }

    @Override
    public int getExperienceStored() {
        return current;
    }

    @Override
    public int getMaxExperienceStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region INBTSerializable

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("current", current);
        nbt.putInt("capacity", capacity);
        nbt.putInt("maxExtract", maxExtract);
        nbt.putInt("maxReceive", maxReceive);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        current = nbt.getInt("current");
        capacity = nbt.getInt("capacity");
        maxExtract = nbt.getInt("maxExtract");
        maxReceive = nbt.getInt("maxReceive");
    }

    @Override
    public int getProgressMin() {
        return 0;
    }

    @Override
    public int getProgressMax() {
        return capacity;
    }

    @Override
    public int getProgressCurrent() {
        return current;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Observer

    public interface IExperienceStorageChangedObserver {
        void onExperienceChanged(@Nonnull ExperienceStorage storage);
    }

    // endregion
}
