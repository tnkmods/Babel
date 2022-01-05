package com.thenatekirby.babel.block;

import com.thenatekirby.babel.machine.entity.DeviceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public abstract class DeviceBlock<T extends DeviceBlockEntity> extends Block implements EntityBlock {
    public DeviceBlock(Properties properties) {
        super(properties);
    }

    public BlockEntityTicker<T> makeTicker(@Nonnull Level level, @Nonnull BlockState blockState, @Nonnull BlockEntityType<T> blockEntityType) {
        return T::serverTick;
    }

    // ====---------------------------------------------------------------------------====
    // region EntityBlock

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <E extends BlockEntity> BlockEntityTicker<E> getTicker(@Nonnull Level level, @Nonnull BlockState blockState, @Nonnull BlockEntityType<E> blockEntityType) {
        return (BlockEntityTicker<E>) makeTicker(level, blockState, (BlockEntityType<T>) blockEntityType);
    }
}
