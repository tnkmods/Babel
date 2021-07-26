package com.thenatekirby.babel.integration.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class BackgroundDrawable implements IDrawable {
    private final int width, height;
    private final ResourceLocation resource;

    public BackgroundDrawable(ResourceLocation drawableId, int width, int height) {
        this.resource = drawableId;
        this.width = width - 5;
        this.height = height - 5;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void draw(MatrixStack matrixStack, int xOffset, int yOffset) {
        RenderSystem.clearCurrentColor();
//        matrixStack.push();
        RenderSystem.pushMatrix();
        Minecraft.getInstance().getTextureManager().bind(this.resource);
        RenderSystem.multMatrix(matrixStack.last().pose());
        GuiUtils.drawTexturedModalRect(matrixStack, 0, 0, 0, 0, width, height, 0);
        RenderSystem.popMatrix();
    }

    public ResourceLocation getResource() {
        return resource;
    }
}
