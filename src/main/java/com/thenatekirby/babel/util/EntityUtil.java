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
        return !livingEntity.canChangeDimensions();
    }

    public static boolean isNonBoss(LivingEntity livingEntity) {
        return livingEntity.canChangeDimensions();
    }

    public static boolean isPlayer(LivingEntity livingEntity) {
        return livingEntity instanceof PlayerEntity;
    }

    public static boolean isTameable(LivingEntity livingEntity) {
        return livingEntity instanceof TameableEntity;
    }

    public static boolean isTamed(LivingEntity livingEntity) {
        return livingEntity instanceof TameableEntity && ((TameableEntity) livingEntity).isTame();
    }

    public static boolean isTamedByPlayer(LivingEntity livingEntity, PlayerEntity playerEntity) {
        if (!isTamed(livingEntity)) {
            return false;
        }

        UUID ownerID = ((TameableEntity) livingEntity).getOwnerUUID();
        if (ownerID == null) {
            return false;
        }

        return ownerID.equals(playerEntity.getUUID());
    }

    public static ITextComponent getDisplayName(@Nonnull String entityId) {
        return EntityType.byString(entityId).map(EntityType::getDescription).orElse(new StringTextComponent(entityId));
    }
}
