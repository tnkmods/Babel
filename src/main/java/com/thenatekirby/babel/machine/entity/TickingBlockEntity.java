package com.thenatekirby.babel.machine.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public abstract class TickingBlockEntity extends BabelBlockEntity {
    public TickingBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    public static <T extends TickingBlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, T blockEntity) {
        if (level == null || level.isClientSide) {
            return;
        }

        blockEntity.workTick((ServerLevel) level);
    }

    protected abstract void workTick(@Nonnull ServerLevel level);
}
