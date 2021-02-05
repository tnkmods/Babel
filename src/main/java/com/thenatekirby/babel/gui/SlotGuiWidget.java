package com.thenatekirby.babel.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.core.gui.GuiRenderer;
import com.thenatekirby.babel.mod.BabelTextureLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class SlotGuiWidget extends GuiWidget {
    public enum SlotType {
        DEFAULT(18, 18, 0, 0);

        public int width;
        public int height;
        public int textureX;
        public int textureY;

        SlotType(int width, int height, int textureX, int textureY) {
            this.width = width;
            this.height = height;
            this.textureX = textureX;
            this.textureY = textureY;
        }
    }

    private static final ResourceLocation GUI = BabelTextureLocations.GUI.COMPONENTS;
    private SlotType slotType;
    private ResourceLocation defaultLocation;
    private int textureX;
    private int textureY;
    private GuiRenderer renderer;

    public SlotGuiWidget(SlotType slotType, ResourceLocation defaultLocation, GuiRenderer renderer, int x, int y) {
        super(renderer.getGuiLeft() + x - 1, renderer.getGuiTop() + y - 1, slotType.width, slotType.height, NO_MESSAGE_COMPONENT);
        this.defaultLocation = defaultLocation;
        this.renderer = renderer;
        setSlotType(slotType);
        active = false;
    }

    // ====---------------------------------------------------------------------------====

    private void setSlotType(SlotType slotType) {
        this.slotType = slotType;
        this.textureX = slotType.textureX;
        this.textureY = slotType.textureY;
    }

    // ====---------------------------------------------------------------------------====
    // Rendering

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().textureManager.bindTexture(GUI);
        renderer.drawTexturedRect(matrixStack, x, y, textureX, textureY, width, height);
        Minecraft.getInstance().textureManager.bindTexture(defaultLocation);
    }
}
