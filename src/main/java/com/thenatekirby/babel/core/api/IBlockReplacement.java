package com.thenatekirby.babel.core.api;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

// ====---------------------------------------------------------------------------====

public interface IBlockReplacement {
    void overrideDefaultState(BlockState state);
    void overrideStateContainer(StateDefinition<Block, BlockState> container);
}
