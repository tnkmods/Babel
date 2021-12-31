package com.thenatekirby.babel.gui.slot;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.thenatekirby.babel.babelmod.BabelTextureLocations;
import com.thenatekirby.babel.core.gui.Frame;
import com.thenatekirby.babel.gui.GuiView;
import com.thenatekirby.babel.util.RenderUtil;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class GuiSlot extends GuiView {
    public enum SlotType {
        DEFAULT(18, 18, 0, 0);

        public final int width;
        public final int height;
        public final int textureX;
        public final int textureY;

        SlotType(int width, int height, int textureX, int textureY) {
            this.width = width;
            this.height = height;
            this.textureX = textureX;
            this.textureY = textureY;
        }

        public int getTextureX() {
            return textureX;
        }

        public int getTextureY() {
            return textureY;
        }
    }

    private final SlotType slotType;
    private final GuiView hintView;

    public GuiSlot(int x, int y, SlotType slotType, GuiView hintView) {
        super(x, y, slotType.width, slotType.height);
        this.visible = true;
        this.slotType = slotType;
        this.hintView = hintView;
    }

    // ====---------------------------------------------------------------------------====
    // region Rendering

    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);
        RenderUtil.bindTexture(BabelTextureLocations.GUI.COMPONENTS);
        Frame frame = getFrame().offsetBy(-1, -1);
        drawTexturedRect(poseStack, frame, slotType.textureX, slotType.textureY);

        if (hintView instanceof GuiSlotHintView) {
            RenderSystem.enableDepthTest();
            ((GuiSlotHintView) hintView).renderHintInto(getFrame(), poseStack, getRenderer(), mouseX, mouseY, partialTicks);

            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();

            RenderUtil.color4f(1.0f, 1.0f, 1.0f, 0.5f);
            RenderUtil.bindTexture(BabelTextureLocations.GUI.COMPONENTS);
            drawTexturedRect(poseStack, frame, slotType.textureX, slotType.textureY);
            RenderUtil.resetColor();

            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
        }
    }

    // endregion
}
