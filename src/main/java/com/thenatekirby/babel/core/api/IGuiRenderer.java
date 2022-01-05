package com.thenatekirby.babel.core.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public interface IGuiRenderer {
    int getGuiLeft();
    int getGuiTop();
    int getGuiWidth();
    int getGuiHeight();

    void bindTexture(@Nonnull ResourceLocation resourceLocation);

    @Nonnull
    Font getFont();

    default ItemRenderer getItemRenderer() {
        return Minecraft.getInstance().getItemRenderer();
    }
}
