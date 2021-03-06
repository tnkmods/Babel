package com.thenatekirby.babel.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings({"WeakerAccess", "unused"})
@OnlyIn(Dist.CLIENT)
public class RenderUtil {

    private static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }

    public static ItemRenderer getItemRenderer() {
        return Minecraft.getInstance().getItemRenderer();
    }

    public static void renderItemIntoGui(IItemProvider itemProvider, MatrixStack matrixStack, int xPos, int yPos) {
        renderItemIntoGuiScaled(itemProvider, matrixStack, xPos, yPos, 1);
    }

    public static void renderItemIntoGuiScaled(IItemProvider itemProvider, MatrixStack matrixStack, int xPos, int yPos, int scale) {
        ItemRenderer itemRenderer = getItemRenderer();

        matrixStack.push();
        RenderSystem.enableDepthTest();
        RenderHelper.enableStandardItemLighting();
        if (scale != 1) {
            matrixStack.scale(scale, scale, scale);
        }

        RenderSystem.pushMatrix();
        RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
        itemRenderer.renderItemAndEffectIntoGUI(new ItemStack(itemProvider), xPos, yPos);

        RenderSystem.popMatrix();
        RenderHelper.disableStandardItemLighting();
        RenderSystem.disableDepthTest();
        matrixStack.pop();
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
        RenderSystem.color4f(1, 1, 1, 1);
    }

    public static void setColor4f(int color) {
        RenderSystem.color4f(getRed(color), getGreen(color), getBlue(color), getAlpha(color));
    }

    public static void setColor3f(int color) {
        RenderSystem.color3f(getRed(color), getGreen(color), getBlue(color));
    }

    public static int getDefaultTextColor() {
        return 4210752;
    }

    // ====---------------------------------------------------------------------------====
    // Textures

    public static void bindTexture(ResourceLocation resourceLocation) {
        getMinecraft().getTextureManager().bindTexture(resourceLocation);
    }
}
