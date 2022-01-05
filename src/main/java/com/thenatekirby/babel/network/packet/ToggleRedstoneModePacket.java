package com.thenatekirby.babel.network.packet;

import com.thenatekirby.babel.core.api.IPacketHandler;
import com.thenatekirby.babel.machine.entity.DeviceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// ====---------------------------------------------------------------------------====

public class ToggleRedstoneModePacket {
    private BlockPos blockPos;
    private boolean forward;

    public ToggleRedstoneModePacket(BlockPos blockPos, boolean forward) {
        this.blockPos = blockPos;
        this.forward = forward;
    }

    public static class Handler implements IPacketHandler<ToggleRedstoneModePacket> {
        public static final Handler INSTANCE = new Handler();

        @Override
        public void encode(ToggleRedstoneModePacket packet, FriendlyByteBuf buffer) {
            buffer.writeBlockPos(packet.blockPos);
            buffer.writeBoolean(packet.forward);
        }

        @Override
        public ToggleRedstoneModePacket decode(FriendlyByteBuf buffer) {
            return new ToggleRedstoneModePacket(buffer.readBlockPos(), buffer.readBoolean());
        }

        @Override
        public void handle(ToggleRedstoneModePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer entity = context.getSender();
                if (entity != null) {
                    Level level = entity.level;
                    BlockEntity blockEntity = level.getBlockEntity(packet.blockPos);
                    if (blockEntity instanceof DeviceBlockEntity) {
                        ((DeviceBlockEntity) blockEntity).toggleRedstoneMode(packet.forward);
                    }
                }
            });

            context.setPacketHandled(true);
        }
    }
}
