package com.thenatekirby.babel.block.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class PhantomBlockBehavior {
    public static VoxelShape getCollisionShape(@Nonnull BlockState blockState, @Nonnull BlockGetter block, @Nonnull BlockPos blockPos, @Nonnull CollisionContext context) {
        if (!(context instanceof EntityCollisionContext)) {
            return blockState.getShape(block, blockPos);
        }

        Entity entity = ((EntityCollisionContext) context).getEntity();
        if (entity instanceof Player) {
            return Shapes.empty();
        } else {
            return blockState.getShape(block, blockPos);
        }
    }
}
