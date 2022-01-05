package com.thenatekirby.babel.gui.buttons;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thenatekirby.babel.core.api.IBooleanProvider;
import com.thenatekirby.babel.core.api.IClickableGuiView;
import com.thenatekirby.babel.core.api.IGuiRenderer;
import com.thenatekirby.babel.core.gui.ClickContext;
import com.thenatekirby.babel.core.gui.Frame;
import com.thenatekirby.babel.gui.GuiView;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.Nonnull;
import java.util.List;


// ====---------------------------------------------------------------------------====

public class GuiToggleWorkingAreaButton extends GuiView implements IClickableGuiView {
    private final IBooleanProvider provider;
    private final GuiButton.IOnClickListener listener;

    public GuiToggleWorkingAreaButton(int x, int y, IBooleanProvider provider, GuiButton.IOnClickListener listener) {
        super(x, y, 18, 18);
        this.provider = provider;
        this.listener = listener;
    }

    @Override
    public void drawFrame(Frame frame, PoseStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
        super.drawFrame(frame, matrixStack, renderer, mouseX, mouseY, partialTicks);

        drawTexturedRect(matrixStack, frame, 0, 128);
        drawTexturedRect(matrixStack, frame, 31, 146);
    }

    @Override
    public boolean onClick(@Nonnull ClickContext context) {
        listener.onButtonClick(context);
        playButtonClick();
        return super.onClick(context);
    }

    @Override
    public boolean onRightClick(@Nonnull ClickContext context) {
        listener.onButtonClick(context);
        playButtonClick();
        return super.onRightClick(context);
    }

    @Override
    public void addTooltips(List<Component> tooltips) {
        super.addTooltips(tooltips);

        if (provider.get()) {
            tooltips.add(new TextComponent("Working Area: Visible"));
        } else {
            tooltips.add(new TextComponent("Working Area: Hidden"));
        }

        tooltips.add(new TextComponent("Click to toggle").withStyle(ChatFormatting.GRAY));
    }
}
