package com.thenatekirby.babel.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.api.IProgress;
import com.thenatekirby.babel.core.gui.GuiRenderer;
import com.thenatekirby.babel.mod.BabelTextureLocations;
import com.thenatekirby.babel.util.RenderUtil;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ProgressBarGuiWidget extends GuiWidget {
    public enum BarType {
        DEFAULT(32, 32, 0, 80);

        public int width;
        public int height;
        public int textureX;
        public int textureY;

        BarType(int width, int height, int textureX, int textureY) {
            this.width = width;
            this.height = height;
            this.textureX = textureX;
            this.textureY = textureY;
        }
    }

    private static final ResourceLocation GUI = BabelTextureLocations.GUI.COMPONENTS;
    private BarType barType;
    private ResourceLocation defaultLocation;
    private int textureX;
    private int textureY;
    private GuiRenderer renderer;
    private IProgress progressProvider;

    public ProgressBarGuiWidget(BarType barType, ResourceLocation defaultLocation, IProgress progressProvider, GuiRenderer renderer, int x, int y) {
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
        this.textureY = 80; //barType.textureY;
    }

    // ====---------------------------------------------------------------------------====
    // Rendering

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderUtil.bindTexture(GUI);

        int progressWidth = (int)(progressProvider.getProgress() * barType.width);

        renderer.drawTexturedRect(matrixStack, x, y, textureX, textureY, width, height);
        renderer.drawTexturedRect(matrixStack, x, y, textureX + barType.width, textureY, progressWidth, barType.height);

        RenderUtil.bindTexture(defaultLocation);
    }
}
