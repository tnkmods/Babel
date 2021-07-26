package com.thenatekirby.babel.network;

import com.thenatekirby.babel.api.IPacketHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NetworkManager {
    private final ResourceLocation channelName;
    private final SimpleChannel channel;
    private int ID = 0;

    public NetworkManager(@Nonnull ResourceLocation channelName) {
        this.channelName = channelName;
        this.channel = NetworkRegistry.newSimpleChannel(channelName, () -> "1.0", val -> true, val -> true);
    }

    protected SimpleChannel getChannel() {
        return channel;
    }

    protected int nextID() {
        return ID++;
    }

    public <T> void register(Class<T> messageType, BiConsumer<T, PacketBuffer> encoder, Function<PacketBuffer, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        getChannel().registerMessage(nextID(), messageType, encoder, decoder, messageConsumer);
    }

    public <T> void register(Class<T> messageType, IPacketHandler<T> packetHandler) {
        getChannel().registerMessage(nextID(), messageType, packetHandler::encode, packetHandler::decode, packetHandler::handle);
    }

    public <T> void sendToPlayer(ServerPlayerEntity entity, T packet) {
        getChannel().sendTo(packet, entity.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public <T> void sendToServer(T packet) {
        getChannel().sendToServer(packet);
    }
}
