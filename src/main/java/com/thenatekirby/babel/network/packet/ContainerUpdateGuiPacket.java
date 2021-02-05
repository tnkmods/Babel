package com.thenatekirby.babel.network.packet;

import com.thenatekirby.babel.api.IPacketHandler;
import com.thenatekirby.babel.core.container.BabelContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ContainerUpdateGuiPacket extends BabelPacket {
    private final int windowId;
    private final PacketBuffer buffer;

    public ContainerUpdateGuiPacket(int windowId, PacketBuffer buffer) {
        this.windowId = windowId;
        this.buffer = buffer;
    }

    public PacketBuffer getBuffer() {
        return buffer;
    }

    public static class Handler implements IPacketHandler<ContainerUpdateGuiPacket> {
        public static final Handler INSTANCE = new Handler();

        @Override
        public void encode(ContainerUpdateGuiPacket packet, PacketBuffer buffer) {
            buffer.writeInt(packet.windowId);
            buffer.writeBytes(packet.buffer);
        }

        @Override
        public ContainerUpdateGuiPacket decode(PacketBuffer buffer) {
            int windowId = buffer.readInt();
            return new ContainerUpdateGuiPacket(windowId, buffer);
        }

        @Override
        public void handle(ContainerUpdateGuiPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                PlayerEntity playerEntity = Minecraft.getInstance().player;

                if (playerEntity != null && playerEntity.openContainer instanceof BabelContainer && playerEntity.openContainer.windowId == packet.windowId) {
                    ((BabelContainer) playerEntity.openContainer).receive(packet);
                }
            });

            context.setPacketHandled(true);
        }
    }
}
