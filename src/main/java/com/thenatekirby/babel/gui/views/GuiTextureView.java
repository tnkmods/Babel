package com.thenatekirby.babel.gui.views;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thenatekirby.babel.core.api.IGuiRenderer;
import com.thenatekirby.babel.core.gui.Frame;
import com.thenatekirby.babel.gui.GuiView;

// ====---------------------------------------------------------------------------====

public class GuiTextureView extends GuiView {
    private final int textureX;
    private final int textureY;

    public GuiTextureView(int x, int y, int width, int height, IGuiTexture texture) {
        super(x, y, width, height);
        this.textureX = texture.getTextureX();
        this.textureY = texture.getTextureY();
    }

    @Override
    public void drawFrame(Frame frame, PoseStack poseStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
        super.drawFrame(frame, poseStack, renderer, mouseX, mouseY, partialTicks);
        drawTexturedRect(poseStack, frame, textureX, textureY);
    }

    // ====---------------------------------------------------------------------------====

    public interface IGuiTexture {
        int getTextureX();
        int getTextureY();
    }
}
