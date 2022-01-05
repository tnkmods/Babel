package com.thenatekirby.babel.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

// ====---------------------------------------------------------------------------====

public class ServerUtil {
    public static boolean isClientLevel(@Nonnull Level world) {
        return world.isClientSide;
    }

    public static void ifServer(@Nullable Level world, @Nonnull Consumer<ServerLevel> consumer) {
        if (world == null || world.isClientSide) {
            return;
        }

        consumer.accept((ServerLevel) world);
    }

    public static void ifServer(@Nullable BlockEntity tileEntity, @Nonnull Consumer<ServerLevel> consumer) {
        if (tileEntity == null) {
            return;
        }

        ifServer(tileEntity.getLevel(), consumer);
    }
}
