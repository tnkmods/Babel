package com.thenatekirby.babel.core.api;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// ====---------------------------------------------------------------------------====

public interface IPacketHandler<T> {
    void encode(T packet, FriendlyByteBuf buffer);
    T decode(FriendlyByteBuf buffer);
    void handle(T packet, Supplier<NetworkEvent.Context> contextSupplier);
}
