package com.thenatekirby.babel.gui.bars;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.api.IGuiRenderer;
import com.thenatekirby.babel.api.IProgress;
import com.thenatekirby.babel.core.gui.GuiRenderer;
import com.thenatekirby.babel.gui.GuiView;
import com.thenatekirby.babel.gui.core.Frame;

import javax.annotation.Nullable;

public class GuiVerticalBar extends GuiView {
    private final int textureX;
    private final int textureXFilled;
    private final int textureY;
    private final int textureYFilled;

    public GuiVerticalBar(int x, int y, int width, int height, IVerticalBarType barType) {
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
    public void drawFrame(Frame frame, MatrixStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
        IProgress progressProvider = getProgressProvider();

        if (progressProvider != null) {
            int progressHeight = (int) (progressProvider.getProgress() * frame.height);
            int offset = frame.height - progressHeight;

            drawTexturedRect(matrixStack, frame, textureX, textureY);
            drawTexturedRect(matrixStack, frame.x, frame.y + offset, textureXFilled, textureYFilled + (frame.height - progressHeight), frame.width, progressHeight);
        }
    }

    public interface IVerticalBarType {
        int getTextureX(boolean filled);
        int getTextureY(boolean filled);
    }
}
