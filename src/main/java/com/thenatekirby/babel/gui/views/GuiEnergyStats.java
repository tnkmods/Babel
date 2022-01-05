package com.thenatekirby.babel.gui.views;

import com.thenatekirby.babel.core.api.IEnergyStatsProvider;
import com.thenatekirby.babel.machine.config.EnergyStats;
import com.thenatekirby.babel.util.StringFormatting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.List;

// ====---------------------------------------------------------------------------====

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

    // ====---------------------------------------------------------------------------====
    // region Tooltips

    @Override
    public void addTooltips(List<Component> tooltips) {
        super.addTooltips(tooltips);

        // TODO: Localize
        EnergyStats stats = provider.getEnergyStats();
        String accepts = StringFormatting.formatNumber(stats.getAccepts());
        String consumes = StringFormatting.formatNumber(stats.getConsumes());
        tooltips.add(new TextComponent("Energy:"));
        tooltips.add(new TextComponent("Accepting " + accepts + " FE/t").withStyle(ChatFormatting.GRAY));
        tooltips.add(new TextComponent("Consuming " + consumes + " FE/t").withStyle(ChatFormatting.GRAY));

        if (stats.getEfficiency() != 1.0f || stats.getSpeed() != 1.0f) {
            String efficiency = StringFormatting.formatNumber(stats.getEfficiency());
            tooltips.add(new TextComponent("Efficiency " + efficiency + "x").withStyle(ChatFormatting.DARK_GRAY));

            String speed = StringFormatting.formatNumber(stats.getSpeed());
            tooltips.add(new TextComponent("Speed: " + speed + "x").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    // endregion
}
