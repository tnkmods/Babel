package com.thenatekirby.babel.gui.tab;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.api.IBackgroundGuiView;
import com.thenatekirby.babel.api.IGuiRenderer;
import com.thenatekirby.babel.core.gui.GuiRenderer;
import com.thenatekirby.babel.gui.GuiView;
import com.thenatekirby.babel.gui.core.Frame;
import net.minecraft.client.renderer.Rectangle2d;

import javax.annotation.Nullable;

public class GuiTabView extends GuiView implements IBackgroundGuiView {
    public enum TabType {
        LEFT_LARGE(25, 88, 231, 0),
        RIGHT(25, 25, 0, 96);
        public int width;
        public int height;
        public int textureX;
        public int textureY;

        TabType(int width, int height, int textureX, int textureY) {
            this.width = width;
            this.height = height;
            this.textureX = textureX;
            this.textureY = textureY;
        }
    }

    private final TabType tabType;

    public GuiTabView(TabType tabType, int x, int y) {
        super(x, y, tabType.width, tabType.height);

        this.tabType = tabType;
    }

    @Nullable
    @Override
    public Rectangle2d getExtraBounds() {
        return getFrame().toRectangle2d();
    }

    @Override
    public void drawFrame(Frame frame, MatrixStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
//        drawTexturedRect(matrixStack, frame, tabType.textureX, tabType.textureY);
    }

    @Override
    protected void drawBgFrame(Frame frame, MatrixStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
        drawTexturedRect(matrixStack, frame, tabType.textureX, tabType.textureY);
    }
}
