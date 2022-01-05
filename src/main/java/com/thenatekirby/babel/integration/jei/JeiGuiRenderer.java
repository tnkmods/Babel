package com.thenatekirby.babel.integration.jei;

import com.thenatekirby.babel.core.api.IGuiRenderer;
import com.thenatekirby.babel.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class JeiGuiRenderer implements IGuiRenderer {
    @Override
    public int getGuiLeft() {
        return 0;
    }

    @Override
    public int getGuiTop() {
        return 0;
    }

    @Override
    public int getGuiWidth() {
        return 0;
    }

    @Override
    public int getGuiHeight() {
        return 0;
    }

    @Override
    public void bindTexture(@Nonnull ResourceLocation resourceLocation) {
        RenderUtil.bindTexture(resourceLocation);
    }

    @Nonnull
    @Override
    public Font getFont() {
        return Minecraft.getInstance().font;
    }
}
