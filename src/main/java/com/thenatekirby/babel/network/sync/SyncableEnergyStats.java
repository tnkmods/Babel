package com.thenatekirby.babel.network.sync;

import com.thenatekirby.babel.api.IEnergyStatsProvider;
import com.thenatekirby.babel.api.ISyncable;
import com.thenatekirby.babel.core.energy.EnergyStats;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;

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
    public void write(@Nonnull PacketBuffer packetBuffer) {
        packetBuffer.writeInt(energyStats.getAccepts());
        packetBuffer.writeInt(energyStats.getConsumes());
        packetBuffer.writeFloat(energyStats.getSpeed());
        packetBuffer.writeFloat(energyStats.getEfficiency());
    }

    @Override
    public void read(@Nonnull PacketBuffer packetBuffer) {
        energyStats.setAccepts(packetBuffer.readInt());
        energyStats.setConsumes(packetBuffer.readInt());
        energyStats.setSpeed(packetBuffer.readFloat());
        energyStats.setEfficiency(packetBuffer.readFloat());
    }
}
