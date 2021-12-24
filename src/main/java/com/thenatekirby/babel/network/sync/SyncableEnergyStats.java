package com.thenatekirby.babel.network.sync;

import com.thenatekirby.babel.core.api.IEnergyStatsProvider;
import com.thenatekirby.babel.core.api.ISyncable;
import com.thenatekirby.babel.machine.config.EnergyStats;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class SyncableEnergyStats implements IEnergyStatsProvider, ISyncable {
    private final EnergyStats energyStats;

    private SyncableEnergyStats(@Nonnull EnergyStats energyStats) {
        this.energyStats = energyStats;
    }

    public static SyncableEnergyStats from(@Nonnull EnergyStats energyStats) {
        return new SyncableEnergyStats(energyStats);
    }

    @Nonnull
    @Override
    public EnergyStats getEnergyStats() {
        return energyStats;
    }

    @Override
    public void write(@Nonnull FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(energyStats.getAccepts());
        packetBuffer.writeInt(energyStats.getConsumes());
        packetBuffer.writeFloat(energyStats.getSpeed());
        packetBuffer.writeFloat(energyStats.getEfficiency());
    }

    @Override
    public void read(@Nonnull FriendlyByteBuf packetBuffer) {
        energyStats.setAccepts(packetBuffer.readInt());
        energyStats.setConsumes(packetBuffer.readInt());
        energyStats.setSpeed(packetBuffer.readFloat());
        energyStats.setEfficiency(packetBuffer.readFloat());
    }
}
