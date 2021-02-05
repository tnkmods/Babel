package com.thenatekirby.babel.api;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IPacketHandler<T> {
    void encode(T packet, PacketBuffer buffer);
    T decode(PacketBuffer buffer);
    void handle(T packet, Supplier<NetworkEvent.Context> contextSupplier);
}
