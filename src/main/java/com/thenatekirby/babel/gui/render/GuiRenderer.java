package com.thenatekirby.babel.gui.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thenatekirby.babel.core.api.IGuiRenderer;
import com.thenatekirby.babel.machine.gui.BabelMenuScreen;
import com.thenatekirby.babel.util.RenderUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.GuiUtils;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class GuiRenderer implements IGuiRenderer {
    private final BabelMenuScreen<?> screen;

    public GuiRenderer(BabelMenuScreen<?> screen) {
        this.screen = screen;
    }

    @Override
    public void bindTexture(@Nonnull ResourceLocation resourceLocation) {
        RenderUtil.bindTexture(resourceLocation);
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
    public Font getFont() {
        return screen.getFont();
    }

    public void renderTooltip(@Nonnull PoseStack matrixStack, @Nonnull TextComponent textComponent, int mouseX, int mouseY) {
        this.renderTooltips(matrixStack, Collections.singletonList(textComponent), mouseX, mouseY);
    }

    public void renderTooltips(@Nonnull PoseStack matrixStack, @Nonnull List<TextComponent> toolTips, int mouseX, int mouseY) {
//        GuiUtils.drawHoveringText(matrixStack, toolTips, mouseX, mouseY, getGuiWidth(), getGuiHeight(), -1, getFontRenderer());
    }
}
