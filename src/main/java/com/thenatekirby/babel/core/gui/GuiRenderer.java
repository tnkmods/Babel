package com.thenatekirby.babel.core.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class GuiRenderer {
    private BabelContainerScreen<?> screen;

    public GuiRenderer(BabelContainerScreen<?> screen) {
        this.screen = screen;
    }

    public void drawTexturedRect(@Nonnull MatrixStack matrixStack, int x, int y, int textureX, int textureY, int width, int height) {
        screen.blit(matrixStack, x, y, textureX, textureY, width, height);
    }

    public void drawTexturedIcon(@Nonnull MatrixStack matrixStack, int x, int y, int width, int height, TextureAtlasSprite sprite) {
        ContainerScreen.blit(matrixStack, x, y, screen.getBlitOffset(), width, height, sprite);
    }

    public int getGuiLeft() {
        return screen.getGuiLeft();
    }

    public int getGuiTop() {
        return screen.getGuiTop();
    }

    public int getGuiWidth() {
        return screen.width;
    }

    public int getGuiHeight() {
        return screen.height;
    }

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
