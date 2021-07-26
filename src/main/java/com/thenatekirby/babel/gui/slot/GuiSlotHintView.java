package com.thenatekirby.babel.gui.slot;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.api.IGuiRenderer;
import com.thenatekirby.babel.gui.GuiView;
import com.thenatekirby.babel.gui.core.Frame;
import com.thenatekirby.babel.gui.core.GuiTextureView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nonnull;

public class GuiSlotHintView extends GuiView {
    private IItemProvider itemProvider;

    public GuiSlotHintView(IItemProvider itemProvider) {
        super(0, 0, 14, 14);
        this.itemProvider = itemProvider;
    }

    public void renderHintInto(@Nonnull Frame frame, MatrixStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
        renderer.bindTexture(getTextureLocation());

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = new ItemStack(itemProvider);
        itemRenderer.renderItemAndEffectIntoGuiWithoutEntity(itemStack, frame.x, frame.y);
    }
}
