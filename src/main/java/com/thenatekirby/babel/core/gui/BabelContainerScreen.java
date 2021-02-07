package com.thenatekirby.babel.core.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.core.container.BabelContainer;
import com.thenatekirby.babel.core.slots.BabelSlot;
import com.thenatekirby.babel.gui.SlotGuiWidget;
import com.thenatekirby.babel.mod.BabelTextureLocations;
import com.thenatekirby.babel.util.InventoryUtil;
import com.thenatekirby.babel.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class BabelContainerScreen<T extends BabelContainer> extends ContainerScreen<T> {
    public BabelContainerScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();

        GuiRenderer guiRenderer = new GuiRenderer(this);
        ResourceLocation backgroundResourceLocation = getBackgroundResourceLocation();

        initGuiWidgets(guiRenderer, backgroundResourceLocation);

        List<Widget> widgets = container.inventorySlots
                .stream()
                .filter(slot -> slot instanceof BabelSlot)
                .map(slot -> new SlotGuiWidget(SlotGuiWidget.SlotType.DEFAULT, backgroundResourceLocation, guiRenderer, slot.xPos, slot.yPos))
                .collect(Collectors.toList());

        addWidgets(widgets);
    }

    public void initGuiWidgets(@Nonnull GuiRenderer renderer, @Nonnull ResourceLocation backgroundResourceLocation) {
    }

    // ====---------------------------------------------------------------------------====
    // region Getters

    public FontRenderer getFontRenderer() {
        return font;
    }

    protected ResourceLocation getBackgroundResourceLocation() {
        return BabelTextureLocations.GUI.GUI_BLANK;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Helpers

    public void addWidgets(Widget... widgets) {
        for (Widget widget : widgets) {
            addButton(widget);
        }
    }

    public void addWidgets(List<Widget> widgets) {
        for (Widget widget : widgets) {
            addButton(widget);
        }
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Lifecycle

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        matrixStack.push();

        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(matrixStack, mouseX, mouseY);

        ItemStack mouseHeldItemStack = InventoryUtil.getPlayerMouseHeldItemStack();
        if (mouseHeldItemStack.isEmpty()) {

        }

        matrixStack.pop();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {
        RenderUtil.resetColor();

        this.drawCenteredTitleText(matrixStack, RenderUtil.getDefaultTextColor());
        this.drawInventoryTextAt(matrixStack,8.0F, (float)(this.ySize - 96 + 2), RenderUtil.getDefaultTextColor());

        for (Widget widget : this.buttons) {
            if (widget.isMouseOver(mouseX, mouseY)) {
                widget.renderToolTip(matrixStack, mouseX - guiLeft, mouseY - guiTop);
                break;
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderUtil.resetColor();

        ResourceLocation resourceLocation = getBackgroundResourceLocation();
        RenderUtil.bindTexture(resourceLocation);

        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;

        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Text Rendering

    protected void drawTitleTextAt(@Nonnull MatrixStack matrixStack, float x, float y) {
        this.drawTitleTextAt(matrixStack, x, y, RenderUtil.getDefaultTextColor());
    }

    protected void drawTitleTextAt(@Nonnull MatrixStack matrixStack, float x, float y, int color) {
        this.font.drawString(matrixStack, this.title.getString(), x, y, color);
    }

    protected void drawCenteredTitleText(@Nonnull MatrixStack matrixStack, int color) {
        String title = this.title.getString();
        this.font.drawString(matrixStack, title, (float)(this.xSize / 2 - this.font.getStringWidth(title) / 2), 6.0F, color);
    }

    protected void drawInventoryTextAt(@Nonnull MatrixStack matrixStack, float x, float y) {
        this.drawInventoryTextAt(matrixStack, x, y, RenderUtil.getDefaultTextColor());
    }

    protected void drawInventoryTextAt(@Nonnull MatrixStack matrixStack, float x, float y, int color) {
        this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(), x, y, color);
    }

    // endregion
}
