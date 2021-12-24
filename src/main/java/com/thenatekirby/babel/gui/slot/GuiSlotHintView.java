package com.thenatekirby.babel.gui.slot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thenatekirby.babel.core.api.IGuiRenderer;
import com.thenatekirby.babel.core.api.IItemProvider;
import com.thenatekirby.babel.core.gui.Frame;
import com.thenatekirby.babel.gui.GuiView;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class GuiSlotHintView extends GuiView {
    private IItemProvider itemProvider;

    public GuiSlotHintView(IItemProvider itemProvider) {
        super(0, 0, 14, 14);
        this.itemProvider = itemProvider;
    }

    public void renderHintInto(@Nonnull Frame frame, PoseStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
        renderer.bindTexture(getTextureLocation());

        ItemRenderer itemRenderer = renderer.getItemRenderer();
        ItemStack itemStack = new ItemStack(itemProvider);
        itemRenderer.renderAndDecorateFakeItem(itemStack, frame.x, frame.y);
    }
}
