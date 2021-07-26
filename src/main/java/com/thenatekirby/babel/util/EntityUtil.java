package com.thenatekirby.babel.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.UUID;

public class EntityUtil {
    public static boolean isAlive(LivingEntity livingEntity) {
        return livingEntity.isAlive();
    }

    public static boolean isBoss(LivingEntity livingEntity) {
        return !livingEntity.isNonBoss();
    }

    public static boolean isNonBoss(LivingEntity livingEntity) {
        return livingEntity.isNonBoss();
    }

    public static boolean isPlayer(LivingEntity livingEntity) {
        return livingEntity instanceof PlayerEntity;
    }

    public static boolean isTameable(LivingEntity livingEntity) {
        return livingEntity instanceof TameableEntity;
    }

    public static boolean isTamed(LivingEntity livingEntity) {
        return livingEntity instanceof TameableEntity && ((TameableEntity) livingEntity).isTamed();
    }

    public static boolean isTamedByPlayer(LivingEntity livingEntity, PlayerEntity playerEntity) {
        if (!isTamed(livingEntity)) {
            return false;
        }

        UUID ownerID = ((TameableEntity) livingEntity).getOwnerId();
        if (ownerID == null) {
            return false;
        }

        return ownerID.equals(playerEntity.getUniqueID());
    }

    public static ITextComponent getDisplayName(@Nonnull String entityId) {
        return EntityType.byKey(entityId).map(EntityType::getName).orElse(new StringTextComponent(entityId));
    }
}
