package com.thenatekirby.babel.core.api;

import net.minecraft.world.level.block.Block;
import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public interface IBlockProvider {
    @Nonnull
    Block asBlock();
}
