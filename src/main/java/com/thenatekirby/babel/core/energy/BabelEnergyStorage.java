package com.thenatekirby.babel.core.energy;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.api.IProgress;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;

public class BabelEnergyStorage implements IEnergyStorage, INBTSerializable<CompoundNBT>, IProgress {
    public interface IEnergyStorageChangedObserver {
        void onEnergyChanged(BabelEnergyStorage storage);
    }

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

    public BabelEnergyStorage(EnergyBuffer buffer) {
        this.capacity = buffer.capacity;
        this.maxReceive = buffer.maxReceive;
        this.maxExtract = buffer.maxExtract;
        this.energyStored = buffer.current;
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
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("energyStored", energyStored);
        nbt.putInt("capacity", capacity);
        nbt.putInt("maxExtract", maxExtract);
        nbt.putInt("maxReceive", maxReceive);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setEnergyStored(nbt.getInt("energyStored"));
        setCapacity(nbt.getInt("capacity"));
        setMaxExtract(nbt.getInt("maxExtract"));
        setMaxReceive(nbt.getInt("maxReceive"));
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Packet Buffers

    public void write(@Nonnull PacketBuffer buffer) {
        buffer.writeInt(getEnergyStored());
    }

    public void read(@Nonnull PacketBuffer buffer) {
        setEnergyStored(buffer.readInt());
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
        return energyStored;
    }
}
