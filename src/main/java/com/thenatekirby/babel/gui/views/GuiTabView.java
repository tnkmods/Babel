package com.thenatekirby.babel.gui.views;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thenatekirby.babel.core.api.IBackgroundGuiView;
import com.thenatekirby.babel.core.api.IGuiRenderer;
import com.thenatekirby.babel.core.gui.Frame;
import com.thenatekirby.babel.gui.GuiView;
import net.minecraft.client.renderer.Rect2i;

import javax.annotation.Nullable;

// ====---------------------------------------------------------------------------====

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
    public Rect2i getExtraBounds() {
        return getFrame().toRectangle2d();
    }

    @Override
    public void drawFrame(Frame frame, PoseStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
//        drawTexturedRect(matrixStack, frame, tabType.textureX, tabType.textureY);
    }

    @Override
    protected void drawBgFrame(Frame frame, PoseStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
        drawTexturedRect(matrixStack, frame, tabType.textureX, tabType.textureY);
    }
}
