package com.thenatekirby.babel.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.thenatekirby.babel.core.api.IItemProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

// ====---------------------------------------------------------------------------====

public class RenderUtil {

    private static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    public static ItemRenderer getItemRenderer() {
        return Minecraft.getInstance().getItemRenderer();
    }

    public static void renderItemIntoGui(IItemProvider itemProvider, PoseStack matrixStack, int xPos, int yPos) {
        renderItemIntoGuiScaled(itemProvider, matrixStack, xPos, yPos, 1);
    }

    public static void renderItemIntoGuiScaled(IItemProvider itemProvider, PoseStack matrixStack, int xPos, int yPos, int scale) {
        // TODO: Render Item Into Gui Scaled
//        ItemRenderer itemRenderer = getItemRenderer();
//        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
//
//        matrixStack.pushPose();
//        RenderSystem.enableDepthTest();
//        RenderHelper.turnBackOn();
//        if (scale != 1) {
//            matrixStack.scale(scale, scale, scale);
//        }
//
//        RenderSystem.pushMatrix();
//        RenderSystem.multMatrix(matrixStack.last().pose());
//        itemRenderer.renderAndDecorateItem(new ItemStack(itemProvider), xPos, yPos);
//
//        RenderSystem.popMatrix();
//        RenderHelper.turnOff();
//        RenderSystem.disableDepthTest();
//        matrixStack.popPose();
    }

    // ====---------------------------------------------------------------------------====
    // Colors

    private static float getRed(int color) {
        return (color >> 16 & 0xFF) / 255.0F;
    }

    private static float getGreen(int color) {
        return (color >> 8 & 0xFF) / 255.0F;
    }

    private static float getBlue(int color) {
        return (color & 0xFF) / 255.0F;
    }

    private static float getAlpha(int color) {
        return (color >> 24 & 0xFF) / 255.0F;
    }

    public static void resetColor() {
        // TODO: Colors
//        RenderSystem.color4f(1, 1, 1, 1);
    }

    public static void setColor4f(int color) {
        // TODO: Colors
//        RenderSystem.color4f(getRed(color), getGreen(color), getBlue(color), getAlpha(color));
    }

    public static void setColor3f(int color) {
        // TODO: Colors
//        RenderSystem.color3f(getRed(color), getGreen(color), getBlue(color));
    }

    public static int getDefaultTextColor() {
        return 4210752;
    }

    // ====---------------------------------------------------------------------------====
    // Textures

    public static void bindTexture(ResourceLocation resourceLocation) {
        getMinecraft().getTextureManager().bindForSetup(resourceLocation);
    }
}
