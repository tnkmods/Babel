package com.thenatekirby.babel.network.packet;

import com.thenatekirby.babel.core.api.IPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// ====---------------------------------------------------------------------------====

public class ContainerUpdateGuiPacket {
    private final int windowId;
    private final FriendlyByteBuf buffer;

    public ContainerUpdateGuiPacket(int windowId, FriendlyByteBuf buffer) {
        this.windowId = windowId;
        this.buffer = buffer;
    }

    public FriendlyByteBuf getBuffer() {
        return buffer;
    }

    public static class Handler implements IPacketHandler<ContainerUpdateGuiPacket> {
        public static final Handler INSTANCE = new Handler();

        @Override
        public void encode(ContainerUpdateGuiPacket packet, FriendlyByteBuf buffer) {
            buffer.writeInt(packet.windowId);
            buffer.writeBytes(packet.buffer);
        }

        @Override
        public ContainerUpdateGuiPacket decode(FriendlyByteBuf buffer) {
            int windowId = buffer.readInt();
            return new ContainerUpdateGuiPacket(windowId, buffer);
        }

        @Override
        public void handle(ContainerUpdateGuiPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {

                Player playerEntity = Minecraft.getInstance().player;

                // TODO: BabelContainer
//                if (playerEntity != null && playerEntity.containerMenu instanceof BabelContainer && playerEntity.containerMenu.containerId == packet.windowId) {
//                    ((BabelContainer) playerEntity.containerMenu).receive(packet);
//                }
            });

            context.setPacketHandled(true);
        }
    }
}
