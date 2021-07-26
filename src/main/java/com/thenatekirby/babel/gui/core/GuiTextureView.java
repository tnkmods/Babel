package com.thenatekirby.babel.gui.core;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.api.IGuiRenderer;
import com.thenatekirby.babel.core.gui.GuiRenderer;
import com.thenatekirby.babel.gui.GuiView;

public class GuiTextureView extends GuiView {
    private final int textureX;
    private final int textureY;

    public GuiTextureView(int x, int y, int width, int height, IGuiTexture texture) {
        super(x, y, width, height);
        this.textureX = texture.getTextureX();
        this.textureY = texture.getTextureY();
    }

    @Override
    public void drawFrame(Frame frame, MatrixStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
        super.drawFrame(frame, matrixStack, renderer, mouseX, mouseY, partialTicks);

        drawTexturedRect(matrixStack, frame, textureX, textureY);
    }

    public interface IGuiTexture {
        int getTextureX();
        int getTextureY();
    }

}
