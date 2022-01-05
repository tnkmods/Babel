package com.thenatekirby.babel.network;

import com.thenatekirby.babel.core.api.IPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

// ====---------------------------------------------------------------------------====

public class NetworkManager {
    private final ResourceLocation channelName;
    private final SimpleChannel channel;
    private int ID = 0;

    public NetworkManager(@Nonnull ResourceLocation channelName) {
        this.channelName = channelName;
        this.channel = NetworkRegistry.newSimpleChannel(channelName, () -> "1.0", val -> true, val -> true);
    }

    // ====---------------------------------------------------------------------------====
    // region Getters

    protected SimpleChannel getChannel() {
        return channel;
    }

    protected ResourceLocation getChannelName() {
        return channelName;
    }

    protected int nextID() {
        return ID++;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Registration

    public <T> void register(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        getChannel().registerMessage(nextID(), messageType, encoder, decoder, messageConsumer);
    }

    public <T> void register(Class<T> messageType, IPacketHandler<T> packetHandler) {
        getChannel().registerMessage(nextID(), messageType, packetHandler::encode, packetHandler::decode, packetHandler::handle);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Packet Sending

    public <T> void sendToPlayer(ServerPlayer entity, T packet) {
        getChannel().sendTo(packet, entity.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public <T> void sendToServer(T packet) {
        getChannel().sendToServer(packet);
    }

    // endregion
}
