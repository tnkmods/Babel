package com.thenatekirby.babel.core.api;

import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public interface ISyncable {
    void write(@Nonnull FriendlyByteBuf packetBuffer);
    void read(@Nonnull FriendlyByteBuf packetBuffer);
}
