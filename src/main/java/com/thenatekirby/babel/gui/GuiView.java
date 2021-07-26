package com.thenatekirby.babel.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.api.IGuiRenderer;
import com.thenatekirby.babel.core.gui.GuiRenderer;
import com.thenatekirby.babel.gui.core.ClickContext;
import com.thenatekirby.babel.gui.core.Frame;
import com.thenatekirby.babel.mod.BabelTextureLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class GuiView extends Widget {
    public static final ITextComponent NO_MESSAGE_COMPONENT = new StringTextComponent("");

    public static final int MOUSE_BUTTON_LEFT = 0;
    public static final int MOUSE_BUTTON_RIGHT = 1;

    private int x = 0;
    private int y = 0;
    private int width = 0;
    private int height = 0;
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
    public Rectangle2d getExtraBounds() {
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
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderer.bindTexture(getTextureLocation());
        drawFrame(frame, matrixStack, renderer, mouseX, mouseY, partialTicks);
    }

    public void renderBg(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderer.bindTexture(getTextureLocation());
        drawBgFrame(frame, matrixStack, renderer, mouseX, mouseY, partialTicks);
    }

    protected void drawFrame(Frame frame, MatrixStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
    }

    protected void drawBgFrame(Frame frame, MatrixStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
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

    public void addTooltips(List<ITextComponent> tooltips) {

    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Rendering

    public void drawTexturedRect(@Nonnull MatrixStack matrixStack, Frame frame, int textureX, int textureY) {
        blit(matrixStack, frame.x, frame.y, textureX, textureY, frame.width, frame.height);
    }

    public void drawTexturedRect(@Nonnull MatrixStack matrixStack, int x, int y, int textureX, int textureY, int width, int height) {
        blit(matrixStack, x, y, textureX, textureY, width, height);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Helpers

    protected void playButtonClick() {
        Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    // endregion
}
