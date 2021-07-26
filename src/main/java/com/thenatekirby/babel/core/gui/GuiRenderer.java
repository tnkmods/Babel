package com.thenatekirby.babel.core.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.api.IGuiRenderer;
import com.thenatekirby.babel.gui.core.Frame;
import com.thenatekirby.babel.util.RenderUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class GuiRenderer implements IGuiRenderer {
    private BabelContainerScreen<?> screen;
    private ResourceLocation currentLocation = null;

    public GuiRenderer(BabelContainerScreen<?> screen) {
        this.screen = screen;
    }

    @Override
    public void bindTexture(@Nonnull ResourceLocation resourceLocation) {
        if (!resourceLocation.equals(currentLocation)) {
//            currentLocation = resourceLocation;
            RenderUtil.bindTexture(resourceLocation);
        }
    }

    @Override
    public int getGuiLeft() {
        return screen.getGuiLeft();
    }

    @Override
    public int getGuiTop() {
        return screen.getGuiTop();
    }

    @Override
    public int getGuiWidth() {
        return screen.getXSize();
    }

    @Override
    public int getGuiHeight() {
        return screen.getYSize();
    }

    @Override
    @Nonnull
    public FontRenderer getFontRenderer() {
        return screen.getFontRenderer();
    }

    public void renderTooltip(@Nonnull MatrixStack matrixStack, @Nonnull ITextComponent textComponent, int mouseX, int mouseY) {
        this.renderTooltips(matrixStack, Collections.singletonList(textComponent), mouseX, mouseY);
    }

    public void renderTooltips(@Nonnull MatrixStack matrixStack, @Nonnull List<ITextComponent> toolTips, int mouseX, int mouseY) {
        GuiUtils.drawHoveringText(matrixStack, toolTips, mouseX, mouseY, getGuiWidth(), getGuiHeight(), -1, getFontRenderer());
    }

}
