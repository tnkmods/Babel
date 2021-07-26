package com.thenatekirby.babel.core.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.api.IBackgroundGuiView;
import com.thenatekirby.babel.api.IClickableGuiView;
import com.thenatekirby.babel.core.container.BabelContainer;
import com.thenatekirby.babel.core.slots.BabelSlot;
import com.thenatekirby.babel.gui.GuiView;
import com.thenatekirby.babel.gui.core.ClickContext;
import com.thenatekirby.babel.gui.core.ViewBuilder;
import com.thenatekirby.babel.gui.slot.GuiSlot;
import com.thenatekirby.babel.mod.BabelTextureLocations;
import com.thenatekirby.babel.util.RenderUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BabelContainerScreen<T extends BabelContainer> extends ContainerScreen<T> {
    public BabelContainerScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    private List<Rectangle2d> extraBounds = new ArrayList<>();
    private List<GuiView> subviews = new ArrayList<>();
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
            Rectangle2d extraBounds = view.getExtraBounds();
            if (extraBounds != null) {
                this.extraBounds.add(extraBounds);
            }
        }

        this.buttons.addAll(
                menu.slots
                        .stream()
                        .filter(slot -> slot instanceof BabelSlot)
                        .map(slot -> new GuiSlot(slot.x, slot.y, GuiSlot.SlotType.DEFAULT, ((BabelSlot) slot).getHintView()))
                        .map(guiSlot -> guiSlot.setRenderer(renderer))
                        .collect(Collectors.toList())
        );
    }

    public void initScreen(ViewBuilder builder) {
    }

    // ====---------------------------------------------------------------------------====
    // region Getters

    public FontRenderer getFontRenderer() {
        return font;
    }

    protected ResourceLocation getBackgroundResourceLocation() {
        return BabelTextureLocations.GUI.GUI_BLANK;
    }

    public List<Rectangle2d> getExtraBounds() {
        return extraBounds;
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
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
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
    protected void renderLabels(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {
        RenderUtil.resetColor();

        this.drawTitleTextAt(matrixStack, 8.0F, 8.0F);
//        this.drawCenteredTitleText(matrixStack, RenderUtil.getDefaultTextColor());
        this.drawInventoryTextAt(matrixStack,8.0F, (float)(this.imageHeight - 96 + 2), RenderUtil.getDefaultTextColor());

        for (Widget widget : this.buttons) {
            if (widget.isMouseOver(mouseX, mouseY)) {
                widget.renderToolTip(matrixStack, mouseX - leftPos, mouseY - topPos);
                break;
            }
        }
    }

    @Override
    protected void renderBg(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderUtil.resetColor();

        ResourceLocation resourceLocation = getBackgroundResourceLocation();
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

    protected void drawTooltips(@Nonnull MatrixStack matrixStack) {
        GuiView targetView = getClosestSubviewToMouse();
        if (targetView != null) {
            List<ITextComponent> tooltips = new ArrayList<>();
            targetView.addTooltips(tooltips);

            GuiUtils.drawHoveringText(matrixStack, tooltips, pointX + leftPos, pointY + topPos, width, height, -1, font);
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

    protected void drawTitleTextAt(@Nonnull MatrixStack matrixStack, float x, float y) {
        this.drawTitleTextAt(matrixStack, x, y, RenderUtil.getDefaultTextColor());
    }

    protected void drawTitleTextAt(@Nonnull MatrixStack matrixStack, float x, float y, int color) {
        this.font.draw(matrixStack, this.title.getString(), x, y, color);
    }

    protected void drawCenteredTitleText(@Nonnull MatrixStack matrixStack, int color) {
        String title = this.title.getString();
        this.font.draw(matrixStack, title, (float)(this.imageWidth / 2 - this.font.width(title) / 2), 6.0F, color);
    }

    protected void drawInventoryTextAt(@Nonnull MatrixStack matrixStack, float x, float y) {
        this.drawInventoryTextAt(matrixStack, x, y, RenderUtil.getDefaultTextColor());
    }

    protected void drawInventoryTextAt(@Nonnull MatrixStack matrixStack, float x, float y, int color) {
        this.font.draw(matrixStack, this.inventory.getDisplayName().getString(), x, y, color);
    }

    // endregion

}
