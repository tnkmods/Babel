package com.thenatekirby.babel.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thenatekirby.babel.babelmod.BabelTextureLocations;
import com.thenatekirby.babel.core.api.IGuiRenderer;
import com.thenatekirby.babel.core.gui.ClickContext;
import com.thenatekirby.babel.core.gui.Frame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class GuiView extends AbstractWidget {
    public static final Component NO_MESSAGE_COMPONENT = new TextComponent("");

    public static final int MOUSE_BUTTON_LEFT = 0;
    public static final int MOUSE_BUTTON_RIGHT = 1;

    private int x;
    private int y;
    private int width;
    private int height;
    private Frame frame = Frame.ZERO;

    private IGuiRenderer renderer;

    public GuiView() {
        this(0, 0, 0, 0);
    }

    public GuiView(@Nonnull Frame frame) {
        this(frame.x, frame.y, frame.width, frame.height);
    }

    public GuiView(int x, int y, int width, int height) {
        super(x, y, width, height, NO_MESSAGE_COMPONENT);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = true;
        this.active = false;
    }

    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    public ResourceLocation getTextureLocation() {
        return BabelTextureLocations.GUI.COMPONENTS;
    }

    public void setFrame(@Nonnull Frame frame) {
        this.x = frame.x;
        this.y = frame.y;
        this.width = frame.width;
        this.height = frame.height;
        this.recalculateFrame();
    }

    public void setFrame(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.recalculateFrame();
    }

    public GuiView setRenderer(@Nonnull IGuiRenderer renderer) {
        this.renderer = renderer;
        this.recalculateFrame();
        return this;
    }

    public IGuiRenderer getRenderer() {
        return renderer;
    }

    @Nullable
    public Rect2i getExtraBounds() {
        return null;
    }

    public Frame getFrame() {
        return frame;
    }

    private void recalculateFrame() {
        if (this.renderer != null) {
            this.frame = new Frame(x + renderer.getGuiLeft(), y + renderer.getGuiTop(), width, height);
        } else {
            this.frame = new Frame(x, y, width, height);
        }
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Info

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.visible && mouseX >= (double)this.x && mouseY >= (double)this.y && mouseX < (double)(this.x + this.width) && mouseY < (double)(this.y + this.height);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Lifecycle

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderer.bindTexture(getTextureLocation());
        drawFrame(frame, matrixStack, renderer, mouseX, mouseY, partialTicks);
    }

    public void renderBg(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderer.bindTexture(getTextureLocation());
        drawBgFrame(frame, matrixStack, renderer, mouseX, mouseY, partialTicks);
    }

    protected void drawFrame(Frame frame, PoseStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
    }

    protected void drawBgFrame(Frame frame, PoseStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
    }

    protected boolean onRightClick(@Nonnull ClickContext context) {
        return true;
    }

    protected boolean onClick(@Nonnull ClickContext context) {
        return true;
    }

    public boolean handleClick(@Nonnull ClickContext context) {
        if (context.buttonIndex == MOUSE_BUTTON_RIGHT) {
            return onRightClick(context);
        } else {
            return onClick(context);
        }
    }

    public void addTooltips(List<Component> tooltips) {
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Rendering

    public void drawTexturedRect(@Nonnull PoseStack matrixStack, Frame frame, int textureX, int textureY) {
        blit(matrixStack, frame.x, frame.y, textureX, textureY, frame.width, frame.height);
    }

    public void drawTexturedRect(@Nonnull PoseStack matrixStack, int x, int y, int textureX, int textureY, int width, int height) {
        blit(matrixStack, x, y, textureX, textureY, width, height);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Narration

    @Override
    public void updateNarration(@Nonnull NarrationElementOutput output) {
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Helpers

    protected void playButtonClick() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    // endregion
}
