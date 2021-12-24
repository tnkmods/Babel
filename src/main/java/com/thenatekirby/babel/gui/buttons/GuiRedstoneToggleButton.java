package com.thenatekirby.babel.gui.buttons;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thenatekirby.babel.core.api.IClickableGuiView;
import com.thenatekirby.babel.core.api.IGuiRenderer;
import com.thenatekirby.babel.core.api.IRedstoneModeProvider;
import com.thenatekirby.babel.core.gui.ClickContext;
import com.thenatekirby.babel.core.gui.Frame;
import com.thenatekirby.babel.gui.GuiView;
import com.thenatekirby.babel.machine.config.RedstoneMode;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.Nonnull;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class GuiRedstoneToggleButton extends GuiView implements IClickableGuiView {
    private final IRedstoneModeProvider provider;
    private final IOnRedstoneToggledListener listener;

    public GuiRedstoneToggleButton(int x, int y, IRedstoneModeProvider provider, IOnRedstoneToggledListener listener) {
        super(x, y, 18, 18);
        this.provider = provider;
        this.listener = listener;
    }

    @Override
    public void drawFrame(Frame frame, PoseStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
        super.drawFrame(frame, matrixStack, renderer, mouseX, mouseY, partialTicks);

        RedstoneMode redstoneMode = provider.getRedstoneMode();

        if (redstoneMode.isIgnored()) {
            drawTexturedRect(matrixStack, frame, 18, 128);
        } else if (redstoneMode.whenPowered()) {
            drawTexturedRect(matrixStack, frame, 36, 128);
        } else {
            drawTexturedRect(matrixStack, frame, 54, 128);
        }
    }

    @Override
    public boolean onClick(@Nonnull ClickContext context) {
        listener.onRedstoneToggled(true);
        playButtonClick();
        return super.onClick(context);
    }

    @Override
    public boolean onRightClick(@Nonnull ClickContext context) {
        listener.onRedstoneToggled(false);
        playButtonClick();
        return super.onRightClick(context);
    }

    @Override
    public void addTooltips(List<Component> tooltips) {
        super.addTooltips(tooltips);

        RedstoneMode redstoneMode = provider.getRedstoneMode();
        if (redstoneMode.isIgnored()) {
            tooltips.add(new TextComponent("Redstone Mode: Ignored"));
        } else if (redstoneMode.whenPowered()) {
            tooltips.add(new TextComponent("Redstone Mode: When Powered"));
        } else {
            tooltips.add(new TextComponent("Redstone Mode: When Not Powered"));
        }
    }

    public interface IOnRedstoneToggledListener {
        void onRedstoneToggled(boolean forward);
    }
}
