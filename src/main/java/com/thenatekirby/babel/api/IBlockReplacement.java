package com.thenatekirby.babel.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;

public interface IBlockReplacement {

    void overrideDefaultState(BlockState state);

    void overrideStateContainer(StateContainer<Block, BlockState> container);

}
