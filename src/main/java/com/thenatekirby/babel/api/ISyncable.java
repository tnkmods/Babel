package com.thenatekirby.babel.api;

import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;

public interface ISyncable {
    void write(@Nonnull PacketBuffer packetBuffer);
    void read(@Nonnull PacketBuffer packetBuffer);
}
