package com.thenatekirby.babel.capability.energy;

import com.thenatekirby.babel.core.progress.IProgress;
import com.thenatekirby.babel.machine.config.EnergyBuffer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class BabelEnergyStorage implements IEnergyStorage, INBTSerializable<CompoundTag>, IProgress {
    private int capacity;
    private int maxReceive;
    private int maxExtract;
    private int energyStored;
    private IEnergyStorageChangedObserver energyChangedObserver;

    public BabelEnergyStorage(int capacity, int maxReceive, int maxExtract, int current) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energyStored = current;
    }

    public static BabelEnergyStorage fromBuffer(@Nonnull EnergyBuffer buffer) {
        return new BabelEnergyStorage(buffer.capacity, buffer.maxReceive, buffer.maxExtract, buffer.current);
    }

    // ====---------------------------------------------------------------------------====
    // region Helpers

    private void onEnergyChanged() {
        if (this.energyChangedObserver != null) {
            energyChangedObserver.onEnergyChanged(this);
        }
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setEnergyStored(int energyStored) {
        this.energyStored = energyStored;
        onEnergyChanged();
    }

    public void setEnergyChangedObserver(IEnergyStorageChangedObserver energyChangedObserver) {
        this.energyChangedObserver = energyChangedObserver;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Helpers

    public void consumeEnergy(int energy) {
        this.setEnergyStored(energyStored - energy);
    }

    public boolean hasAtleast(int energy) {
        return this.energyStored >= energy;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region IEnergyStorage

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int received = Math.min(capacity - energyStored, Math.min(maxReceive, this.maxReceive));
        if (!simulate) {
            this.energyStored += received;
            onEnergyChanged();
        }

        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extracted = Math.min(energyStored, Math.min(maxExtract, this.maxExtract));
        if (!simulate) {
            this.energyStored -= extracted;
            onEnergyChanged();
        }

        return extracted;
    }

    @Override
    public int getEnergyStored() {
        return energyStored;
    }

    @Override
    public int getMaxEnergyStored() {
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
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("energyStored", energyStored);
        nbt.putInt("capacity", capacity);
        nbt.putInt("maxExtract", maxExtract);
        nbt.putInt("maxReceive", maxReceive);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        setEnergyStored(nbt.getInt("energyStored"));
        setCapacity(nbt.getInt("capacity"));
        setMaxExtract(nbt.getInt("maxExtract"));
        setMaxReceive(nbt.getInt("maxReceive"));
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Packet Buffers

    public void write(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeInt(getEnergyStored());
    }

    public void read(@Nonnull FriendlyByteBuf buffer) {
        setEnergyStored(buffer.readInt());
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region IProgress

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
        return energyStored;
    }

    // endregion
    // ====---------------------------------------------------------------------------====

    public interface IEnergyStorageChangedObserver {
        void onEnergyChanged(BabelEnergyStorage storage);
    }
}
