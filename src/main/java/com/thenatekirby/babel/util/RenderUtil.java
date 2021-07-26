package com.thenatekirby.babel.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
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
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();

        matrixStack.pushPose();
        RenderSystem.enableDepthTest();
        RenderHelper.turnBackOn();
        if (scale != 1) {
            matrixStack.scale(scale, scale, scale);
        }

        RenderSystem.pushMatrix();
        RenderSystem.multMatrix(matrixStack.last().pose());
        itemRenderer.renderAndDecorateItem(new ItemStack(itemProvider), xPos, yPos);

        RenderSystem.popMatrix();
        RenderHelper.turnOff();
        RenderSystem.disableDepthTest();
        matrixStack.popPose();


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
        getMinecraft().getTextureManager().bind(resourceLocation);
    }
}
