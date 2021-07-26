package com.thenatekirby.babel.gui.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.api.IBooleanProvider;
import com.thenatekirby.babel.api.IClickableGuiView;
import com.thenatekirby.babel.api.IGuiRenderer;
import com.thenatekirby.babel.api.IRedstoneModeProvider;
import com.thenatekirby.babel.core.RedstoneMode;
import com.thenatekirby.babel.gui.GuiView;
import com.thenatekirby.babel.gui.core.ClickContext;
import com.thenatekirby.babel.gui.core.Frame;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.List;

public class GuiToggleWorkingAreaButton extends GuiView implements IClickableGuiView {
    private final IBooleanProvider provider;
    private final GuiButton.IOnClickListener listener;

    public GuiToggleWorkingAreaButton(int x, int y, IBooleanProvider provider, GuiButton.IOnClickListener listener) {
        super(x, y, 18, 18);
        this.provider = provider;
        this.listener = listener;
    }

    @Override
    public void drawFrame(Frame frame, MatrixStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
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
    public void addTooltips(List<ITextComponent> tooltips) {
        super.addTooltips(tooltips);

        if (provider.get()) {
            tooltips.add(new StringTextComponent("Working Area: Visible"));
        } else {
            tooltips.add(new StringTextComponent("Working Area: Hidden"));
        }

        tooltips.add(new StringTextComponent("Click to toggle").withStyle(TextFormatting.GRAY));
//        RedstoneMode redstoneMode = provider.getRedstoneMode();
//        if (redstoneMode.isIgnored()) {
//            tooltips.add(new StringTextComponent("Redstone Mode: Ignored"));
//        } else if (redstoneMode.whenPowered()) {
//            tooltips.add(new StringTextComponent("Redstone Mode: When Powered"));
//        } else {
//            tooltips.add(new StringTextComponent("Redstone Mode: When Not Powered"));
//        }
    }
}
