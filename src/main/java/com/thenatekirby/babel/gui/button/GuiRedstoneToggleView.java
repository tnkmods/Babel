package com.thenatekirby.babel.gui.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.api.IClickableGuiView;
import com.thenatekirby.babel.api.IGuiRenderer;
import com.thenatekirby.babel.api.IRedstoneModeProvider;
import com.thenatekirby.babel.core.RedstoneMode;
import com.thenatekirby.babel.core.gui.GuiRenderer;
import com.thenatekirby.babel.gui.GuiView;
import com.thenatekirby.babel.gui.core.ClickContext;
import com.thenatekirby.babel.gui.core.Frame;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.List;

public class GuiRedstoneToggleView extends GuiView implements IClickableGuiView {
    private final IRedstoneModeProvider provider;
    private final IOnRedstoneToggledListener listener;

    public GuiRedstoneToggleView(int x, int y, IRedstoneModeProvider provider, IOnRedstoneToggledListener listener) {
        super(x, y, 18, 18);
        this.provider = provider;
        this.listener = listener;
    }

    @Override
    public void drawFrame(Frame frame, MatrixStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
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
    public void addTooltips(List<ITextComponent> tooltips) {
        super.addTooltips(tooltips);

        RedstoneMode redstoneMode = provider.getRedstoneMode();
        if (redstoneMode.isIgnored()) {
            tooltips.add(new StringTextComponent("Redstone Mode: Ignored"));
        } else if (redstoneMode.whenPowered()) {
            tooltips.add(new StringTextComponent("Redstone Mode: When Powered"));
        } else {
            tooltips.add(new StringTextComponent("Redstone Mode: When Not Powered"));
        }
    }

    public interface IOnRedstoneToggledListener {
        void onRedstoneToggled(boolean forward);
    }
}
