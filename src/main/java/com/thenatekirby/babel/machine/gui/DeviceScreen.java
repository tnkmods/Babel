package com.thenatekirby.babel.machine.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.thenatekirby.babel.babelmod.BabelTextureLocations;
import com.thenatekirby.babel.core.api.IBackgroundGuiView;
import com.thenatekirby.babel.core.api.IClickableGuiView;
import com.thenatekirby.babel.core.gui.ClickContext;
import com.thenatekirby.babel.gui.GuiView;
import com.thenatekirby.babel.gui.ViewBuilder;
import com.thenatekirby.babel.gui.render.GuiRenderer;
import com.thenatekirby.babel.gui.slot.GuiSlot;
import com.thenatekirby.babel.machine.menu.BabelMenu;
import com.thenatekirby.babel.machine.slot.BabelSlot;
import com.thenatekirby.babel.util.GuiUtil;
import com.thenatekirby.babel.util.RenderUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class DeviceScreen<T extends BabelMenu> extends AbstractContainerScreen<T> {
    public DeviceScreen(T screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    private final List<Rect2i> extraBounds = new ArrayList<>();
    private final List<GuiView> subviews = new ArrayList<>();
    private GuiRenderer renderer;

    private int pointX = 0;
    private int pointY = 0;
    private boolean leftShift = false;
    private boolean rightShift = false;

    @Override
    protected void init() {
        super.init();

        this.renderer = new GuiRenderer(this);
        ViewBuilder builder = new ViewBuilder(renderer, subviews);

        initScreen(builder);
        this.subviews.forEach(view -> view.setRenderer(renderer));

        for (GuiView view : this.subviews) {
            Rect2i extraBounds = view.getExtraBounds();
            if (extraBounds != null) {
                this.extraBounds.add(extraBounds);
            }
        }

        this.renderables.addAll(
                menu.slots
                        .stream()
                        .filter(slot -> slot instanceof BabelSlot)
                        .map(slot -> new GuiSlot(slot.x, slot.y, GuiSlot.SlotType.DEFAULT, ((BabelSlot) slot).getHintView()))
                        .map(guiSlot -> guiSlot.setRenderer(renderer))
                        .toList()
        );
    }

    public void initScreen(ViewBuilder builder) {
    }

    // ====---------------------------------------------------------------------------====
    // region Getters

    public Font getFont() {
        return font;
    }

    protected ResourceLocation getBackgroundResourceLocation() {
        return BabelTextureLocations.GUI.GUI_BLANK;
    }

    public List<Rect2i> getExtraBounds() {
        return extraBounds;
    }

    protected boolean shouldCenterTitleText() {
        return false;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Helpers

    @Nullable
    protected GuiView getClosestSubviewToMouse() {
        if (subviews.isEmpty()) {
            return null;
        }

        for (int idx = subviews.size(); idx > 0; idx--) {
            GuiView subview = subviews.get(idx - 1);
            if (subview.isMouseOver(pointX, pointY)) {
                return subview;
            }
        }

        return null;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Lifecycle

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        matrixStack.pushPose();

        this.pointX = mouseX - getGuiLeft();
        this.pointY = mouseY - getGuiTop();

        renderBackground(matrixStack);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.subviews.forEach(subview -> subview.render(matrixStack, mouseX, mouseY, partialTicks));

        renderTooltip(matrixStack, mouseX, mouseY);
        drawTooltips(matrixStack);
        matrixStack.popPose();
    }

    @Override
    protected void renderLabels(@Nonnull PoseStack matrixStack, int mouseX, int mouseY) {
        RenderUtil.resetColor();

        if (shouldCenterTitleText()) {
            this.drawCenteredTitleText(matrixStack, RenderUtil.getDefaultTextColor());
        } else {
            this.drawTitleTextAt(matrixStack, 8.0F, 8.0F);
        }

        this.drawInventoryTextAt(matrixStack,8.0F, (float)(this.imageHeight - 96 + 2), RenderUtil.getDefaultTextColor());

        for (Widget widgetObj : this.renderables) {
            if (widgetObj instanceof AbstractWidget widget) {
                if (widget.isMouseOver(mouseX, mouseY)) {
                    widget.renderToolTip(matrixStack, mouseX - leftPos, mouseY - topPos);
                    break;
                }
            }
        }
    }

    @Override
    protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        ResourceLocation resourceLocation = getBackgroundResourceLocation();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, resourceLocation);

        RenderUtil.bindTexture(resourceLocation);

        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;

        this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        for (GuiView view : subviews) {
            if (view instanceof IBackgroundGuiView) {
                view.renderBg(matrixStack, mouseX, mouseY, partialTicks);
            }
        }
    }

    protected void drawTooltips(@Nonnull PoseStack matrixStack) {
        GuiView targetView = getClosestSubviewToMouse();
        if (targetView != null) {
            List<Component> tooltips = new ArrayList<>();
            targetView.addTooltips(tooltips);
            GuiUtil.drawHoveringText(matrixStack, tooltips, pointX + leftPos, pointY + topPos, width, font);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        //super.mouseClicked(mouseX, mouseY, button);

        GuiView targetView = getClosestSubviewToMouse();
        if (targetView instanceof IClickableGuiView) {
            boolean shifting = leftShift || rightShift;
            ClickContext context = new ClickContext(mouseX, mouseY, button, shifting);
            return targetView.handleClick(context);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT) {
            leftShift = true;
        }

        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            rightShift = true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT) {
            leftShift = false;
        }

        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            rightShift = false;
        }

        return false;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Text Rendering

    protected void drawTitleTextAt(@Nonnull PoseStack matrixStack, float x, float y) {
        this.drawTitleTextAt(matrixStack, x, y, RenderUtil.getDefaultTextColor());
    }

    protected void drawTitleTextAt(@Nonnull PoseStack matrixStack, float x, float y, int color) {
        this.font.draw(matrixStack, this.title.getString(), x, y, color);
    }

    protected void drawCenteredTitleText(@Nonnull PoseStack matrixStack, int color) {
        String title = this.title.getString();
        this.font.draw(matrixStack, title, (float)(this.imageWidth / 2 - this.font.width(title) / 2), 6.0F, color);
    }

    protected void drawInventoryTextAt(@Nonnull PoseStack matrixStack, float x, float y) {
        this.drawInventoryTextAt(matrixStack, x, y, RenderUtil.getDefaultTextColor());
    }

    protected void drawInventoryTextAt(@Nonnull PoseStack matrixStack, float x, float y, int color) {
        this.font.draw(matrixStack, this.playerInventoryTitle.getString(), x, y, color);
    }

    // endregion

}
