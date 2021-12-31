package com.thenatekirby.babel.util.entity;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class EntityRemovalUtil {
    public static void pickupEntity(@Nonnull Entity entity) {
        entity.remove(Entity.RemovalReason.DISCARDED);
    }

    public static void discardEntity(@Nonnull Entity entity) {
        entity.remove(Entity.RemovalReason.DISCARDED);
    }

    public static void killEntity(@Nonnull Entity entity) {
        entity.remove(Entity.RemovalReason.KILLED);
    }
}
