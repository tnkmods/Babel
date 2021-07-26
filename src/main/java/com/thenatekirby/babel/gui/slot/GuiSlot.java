package com.thenatekirby.babel.gui.slot;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thenatekirby.babel.api.IGuiRenderer;
import com.thenatekirby.babel.core.gui.GuiRenderer;
import com.thenatekirby.babel.gui.GuiView;
import com.thenatekirby.babel.gui.core.Frame;
import com.thenatekirby.babel.mod.BabelTextureLocations;
import com.thenatekirby.babel.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.CraftingScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class GuiSlot extends GuiView {
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

        public int getTextureX() {
            return textureX;
        }

        public int getTextureY() {
            return textureY;
        }
    }

    public GuiSlot(int x, int y) {
        this(x, y, SlotType.DEFAULT, null);
    }

    private SlotType slotType;
    private GuiView hintView;

    public GuiSlot(int x, int y, SlotType slotType, GuiView hintView) {
        super(x, y, slotType.width, slotType.height);
        this.visible = true;
        this.slotType = slotType;
        this.hintView = hintView;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        RenderUtil.bindTexture(BabelTextureLocations.GUI.COMPONENTS);
        Frame frame = getFrame().offsetBy(-1, -1);
        drawTexturedRect(matrixStack, frame, slotType.textureX, slotType.textureY);

        if (hintView instanceof GuiSlotHintView) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.zLevel = 100.0f;

            RenderSystem.enableDepthTest();
            RenderHelper.enableStandardItemLighting();

            ((GuiSlotHintView) hintView).renderHintInto(getFrame(), matrixStack, getRenderer(), mouseX, mouseY, partialTicks);

            RenderSystem.disableDepthTest();

            // Render an overlay to gray out the hint item
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.disableLighting();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.5f);

            RenderUtil.bindTexture(BabelTextureLocations.GUI.COMPONENTS);
            drawTexturedRect(matrixStack, frame, slotType.textureX, slotType.textureY);

            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
        }
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
    }
}
