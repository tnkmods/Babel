package com.thenatekirby.babel.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.api.IProgress;
import com.thenatekirby.babel.core.gui.GuiRenderer;
import com.thenatekirby.babel.mod.BabelTextureLocations;
import com.thenatekirby.babel.util.RenderUtil;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class VerticalBarGuiWidget extends GuiWidget {
    public enum BarType {
        POWER(8, 62, 0, 50, 8, 0);

        public int width;
        public int height;
        public int textureX;
        public int textureY;
        public int fillOffsetX;
        public int fillOffsetY;

        BarType(int width, int height, int textureX, int textureY, int fillOffsetX, int fillOffsetY) {
            this.width = width;
            this.height = height;
            this.textureX = textureX;
            this.textureY = textureY;
            this.fillOffsetX = fillOffsetX;
            this.fillOffsetY = fillOffsetY;
        }
    }

    private static final ResourceLocation GUI = BabelTextureLocations.GUI.COMPONENTS;
    private BarType barType;
    private ResourceLocation defaultLocation;
    private int textureX;
    private int textureY;
    private GuiRenderer renderer;
    private IProgress progressProvider;

    public VerticalBarGuiWidget(BarType barType, ResourceLocation defaultLocation, IProgress progressProvider, GuiRenderer renderer, int x, int y) {
        super(renderer.getGuiLeft() + x - 1, renderer.getGuiTop() + y - 1, barType.width, barType.height, NO_MESSAGE_COMPONENT);
        this.defaultLocation = defaultLocation;
        this.renderer = renderer;
        this.progressProvider = progressProvider;
        setBarType(barType);
        active = false;
    }

    // ====---------------------------------------------------------------------------====

    private void setBarType(BarType barType) {
        this.barType = barType;
        this.textureX = barType.textureX;
        this.textureY = barType.textureY;
    }

    // ====---------------------------------------------------------------------------====
    // Rendering

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderUtil.bindTexture(GUI);

        int progressHeight = (int)(progressProvider.getProgress() * barType.height);
        int offset = barType.height - progressHeight;

        renderer.drawTexturedRect(matrixStack, x, y, textureX, 50, width, height);
        renderer.drawTexturedRect(matrixStack, x, y + offset, barType.fillOffsetX, textureY + (barType.height - progressHeight), width + 1, progressHeight);

        RenderUtil.bindTexture(defaultLocation);
    }

}
