package com.thenatekirby.babel.integration.jei;

import com.thenatekirby.babel.api.IGuiRenderer;
import com.thenatekirby.babel.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

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
    public FontRenderer getFontRenderer() {
        return Minecraft.getInstance().font;
    }
}
