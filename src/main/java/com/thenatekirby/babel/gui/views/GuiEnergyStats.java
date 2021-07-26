package com.thenatekirby.babel.gui.views;

import com.thenatekirby.babel.api.IEnergyStatsProvider;
import com.thenatekirby.babel.core.energy.EnergyStats;
import com.thenatekirby.babel.gui.core.GuiTextureView;
import com.thenatekirby.babel.util.StringFormatting;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

import com.thenatekirby.babel.gui.core.GuiTextureView.IGuiTexture;

public class GuiEnergyStats extends GuiTextureView {
    private static final IGuiTexture TEXTURE = new IGuiTexture() {
        @Override
        public int getTextureX() {
            return 0;
        }

        @Override
        public int getTextureY() {
            return 146;
        }
    };

    private final IEnergyStatsProvider provider;

    public GuiEnergyStats(int x, int y, IEnergyStatsProvider provider) {
        super(x, y, 16, 16, TEXTURE);
        this.provider = provider;
    }

    @Override
    public void addTooltips(List<ITextComponent> tooltips) {
        super.addTooltips(tooltips);

        EnergyStats stats = provider.getEnergyStats();
        String accepts = StringFormatting.formatNumber(stats.getAccepts());
        String consumes = StringFormatting.formatNumber(stats.getConsumes());
        tooltips.add(new StringTextComponent("Energy:"));
        tooltips.add(new StringTextComponent("Accepting " + accepts + " FE/t").withStyle(TextFormatting.GRAY));
        tooltips.add(new StringTextComponent("Consuming " + consumes + " FE/t").withStyle(TextFormatting.GRAY));

        if (stats.getEfficiency() != 1.0f || stats.getSpeed() != 1.0f) {
            String efficiency = StringFormatting.formatNumber(stats.getEfficiency());
            tooltips.add(new StringTextComponent("Efficiency " + efficiency + "x").withStyle(TextFormatting.DARK_GRAY));

            String speed = StringFormatting.formatNumber(stats.getSpeed());
            tooltips.add(new StringTextComponent("Speed: " + speed + "x").withStyle(TextFormatting.DARK_GRAY));
        }
    }
}
