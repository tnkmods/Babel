package com.thenatekirby.babel.util.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EntityType;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class EntityNameUtil {
    public static Component getDisplayName(@Nonnull String entityId) {
        return EntityType.byString(entityId).map(EntityType::getDescription).orElse(new TextComponent(entityId));
    }
}
