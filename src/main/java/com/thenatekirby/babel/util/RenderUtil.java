package com.thenatekirby.babel.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.thenatekirby.babel.core.api.IItemProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

// ====---------------------------------------------------------------------------====

public class RenderUtil {
//    private static Minecraft getMinecraft() {
//        return Minecraft.getInstance();
//    }
//
//    public static ItemRenderer getItemRenderer() {
//        return Minecraft.getInstance().getItemRenderer();
//    }
//    public static void renderItemIntoGui(IItemProvider itemProvider, PoseStack matrixStack, int xPos, int yPos) {
//        renderItemIntoGuiScaled(itemProvider, matrixStack, xPos, yPos, 1);
//    }
//
//    public static void renderItemIntoGuiScaled(IItemProvider itemProvider, PoseStack matrixStack, int xPos, int yPos, int scale) {
//        ItemRenderer itemRenderer = getItemRenderer();
//        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
//
//        matrixStack.pushPose();
//        RenderSystem.enableDepthTest();
////        RenderHelper.turnBackOn();
//        if (scale != 1) {
//            matrixStack.scale(scale, scale, scale);
//        }
//
////        RenderSystem.pushMatrix();
////        RenderSystem.multMatrix(matrixStack.last().pose());
//        itemRenderer.renderAndDecorateItem(new ItemStack(itemProvider), xPos, yPos);
////
////        RenderSystem.popMatrix();
////        RenderHelper.turnOff();
//        RenderSystem.disableDepthTest();
//        matrixStack.popPose();
//    }

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
        color4f(1, 1, 1, 1);
    }

    public static void color4f(int color) {
        RenderSystem.setShaderColor(getRed(color), getGreen(color), getBlue(color), getAlpha(color));
    }

    public static void color4f(float red, float green, float blue, float alpha) {
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    public static void color3f(int color) {
        RenderSystem.setShaderColor(getRed(color), getGreen(color), getBlue(color), 1.0f);
    }

    public static void color3f(float red, float green, float blue) {
        RenderSystem.setShaderColor(red, green, blue, 1.0f);
    }

    public static int getDefaultTextColor() {
        return 4210752;
    }

    // ====---------------------------------------------------------------------------====
    // Textures

    public static void bindTexture(ResourceLocation resourceLocation) {
        RenderSystem.setShaderTexture(0, resourceLocation);
    }

    public static void withPositionTexShader() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
    }
}
