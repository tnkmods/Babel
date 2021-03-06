package com.thenatekirby.babel.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.api.IProgress;
import com.thenatekirby.babel.core.gui.GuiRenderer;
import com.thenatekirby.babel.util.StringFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class EnergyBufferGuiWidget extends VerticalBarGuiWidget {
    public EnergyBufferGuiWidget(ResourceLocation defaultLocation, IProgress progressProvider, GuiRenderer renderer, int x, int y) {
        super(BarType.POWER, defaultLocation, progressProvider, renderer, x, y);
    }

    @Override
    public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.renderToolTip(matrixStack, mouseX, mouseY);

        IProgress progress = getProgressProvider();
        List<ITextComponent> tooltips = new ArrayList<>();
        tooltips.add(new StringTextComponent(StringFormatting.formatNumber(progress.getProgressCurrent()) + "/" + StringFormatting.formatNumber(progress.getProgressMax()) + "FE"));
        GuiUtils.drawHoveringText(matrixStack, tooltips, mouseX, mouseY, width, height, -1, getRenderer().getFontRenderer());
    }
}
