package com.thenatekirby.babel.util;

import com.thenatekirby.babel.entity.InWorldItemEntity;
import com.thenatekirby.babel.registration.DeferredEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class EntityRegistrationUtil {

    public static <T extends InWorldItemEntity> void registerItemEntityRenderer(DeferredEntity<T> entity) {
        RenderingRegistry.registerEntityRenderingHandler(
                entity.getAsEntityType(),
                (IRenderFactory<ItemEntity>) manager -> new ItemRenderer(manager, Minecraft.getInstance().getItemRenderer())
        );

    }
}
