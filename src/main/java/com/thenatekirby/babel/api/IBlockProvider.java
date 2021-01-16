package com.thenatekirby.babel.api;

import net.minecraft.block.Block;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

/**
 * An interface to mimic IItemProvider
 */
public interface IBlockProvider {
    @Nonnull
    Block asBlock();
}
