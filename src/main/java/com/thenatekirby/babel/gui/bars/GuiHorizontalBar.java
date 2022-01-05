package com.thenatekirby.babel.gui.bars;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thenatekirby.babel.core.api.IGuiRenderer;
import com.thenatekirby.babel.core.gui.Frame;
import com.thenatekirby.babel.core.progress.IProgress;
import com.thenatekirby.babel.gui.GuiView;

import javax.annotation.Nullable;

// ====---------------------------------------------------------------------------====

public class GuiHorizontalBar extends GuiView {
    private final int textureX;
    private final int textureXFilled;
    private final int textureY;
    private final int textureYFilled;

    public GuiHorizontalBar(int x, int y, int width, int height, IHorizontalBarType barType) {
        super(x, y, width, height);
        textureX = barType.getTextureX(false);
        textureXFilled = barType.getTextureX(true);
        textureY = barType.getTextureY(false);
        textureYFilled = barType.getTextureY(false);
    }

    @Nullable
    public IProgress getProgressProvider() {
        return null;
    }

    @Override
    public void drawFrame(Frame frame, PoseStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
        IProgress progressProvider = getProgressProvider();

        if (progressProvider != null) {
            int progressWidth = (int)(progressProvider.getProgress() * frame.width);

            drawTexturedRect(matrixStack, frame, textureX, textureY);
            drawTexturedRect(matrixStack, frame.x, frame.y, textureX + frame.width, textureY, progressWidth, frame.height);
        }
    }

    // ====---------------------------------------------------------------------------====

    public interface IHorizontalBarType {
        int getTextureX(boolean filled);
        int getTextureY(boolean filled);
    }
}
