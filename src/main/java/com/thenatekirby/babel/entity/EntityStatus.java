package com.thenatekirby.babel.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

// ====---------------------------------------------------------------------------====

@SuppressWarnings({"ClassCanBeRecord", "unused"})
public class EntityStatus {
    private final LivingEntity livingEntity;

    private EntityStatus(@Nonnull LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }


    // ====---------------------------------------------------------------------------====
    // region Factories

    @Nonnull
    public static EntityStatus of(@Nonnull LivingEntity livingEntity) {
        return new EntityStatus(livingEntity);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Info

    public boolean canChangeDimensions() {
        return livingEntity.canChangeDimensions();
    }

    public boolean isAlive() {
        return livingEntity.isAlive();
    }

    public boolean isBoss() {
        return !canChangeDimensions();
    }

    public boolean isPlayer() {
        return livingEntity instanceof Player;
    }

    public boolean isTameable() {
        return livingEntity instanceof TamableAnimal;
    }

    public boolean isTamed() {
        return isTameable() && ((TamableAnimal) livingEntity).isTame();
    }

    public boolean isTamedBy(@Nonnull Player player) {
        if (!isTamed()) {
            return false;
        }

        UUID ownerId = ((TamableAnimal) livingEntity).getOwnerUUID();
        return ownerId != null && ownerId.equals(player.getUUID());
    }

    // endregion
}
