package com.thenatekirby.babel.network.packet;

import com.thenatekirby.babel.api.IPacketHandler;
import com.thenatekirby.babel.core.tileentity.WorkingTileEntity;
import com.thenatekirby.babel.mod.BabelPackets;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleRedstoneModePacket extends BabelPacket {
    private BlockPos blockPos;
    private boolean forward;

    public ToggleRedstoneModePacket(BlockPos blockPos, boolean forward) {
        this.blockPos = blockPos;
        this.forward = forward;
    }

    public static class Handler implements IPacketHandler<ToggleRedstoneModePacket> {
        public static final Handler INSTANCE = new Handler();

        @Override
        public void encode(ToggleRedstoneModePacket packet, PacketBuffer buffer) {
            buffer.writeBlockPos(packet.blockPos);
            buffer.writeBoolean(packet.forward);
        }

        @Override
        public ToggleRedstoneModePacket decode(PacketBuffer buffer) {
            return new ToggleRedstoneModePacket(buffer.readBlockPos(), buffer.readBoolean());
        }

        @Override
        public void handle(ToggleRedstoneModePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayerEntity entity = context.getSender();
                if (entity != null) {
                    World world = entity.level;
                    TileEntity tileEntity = world.getBlockEntity(packet.blockPos);
                    if (tileEntity instanceof WorkingTileEntity) {
                        ((WorkingTileEntity) tileEntity).toggleRedstoneMode(packet.forward);
                    }
                }
            });

            context.setPacketHandled(true);
        }
    }
}
