package com.thenatekirby.babel.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ServerUtil {
    public static boolean isClientWorld(@Nonnull World world) {
        return world.isClientSide;
    }

    public static void ifServer(@Nullable World world, Consumer<ServerWorld> consumer) {
        if (world == null || world.isClientSide) {
            return;
        }

        consumer.accept((ServerWorld) world);
    }

    public static void ifServer(@Nullable TileEntity tileEntity, Consumer<ServerWorld> consumer) {
        if (tileEntity == null) {
            return;
        }

        ifServer(tileEntity.getLevel(), consumer);
    }
}
