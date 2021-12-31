package com.thenatekirby.babel.gui.bars;

import com.thenatekirby.babel.core.progress.IProgress;
import com.thenatekirby.babel.util.StringFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.Nullable;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class GuiEnergyBufferBar extends GuiVerticalBar {
    private static final IVerticalBarType BAR_TYPE = new IVerticalBarType() {
        @Override
        public int getTextureX(boolean filled) {
            return filled ? 8 : 0;
        }

        @Override
        public int getTextureY(boolean filled) {
            return 18;
        }
    };

    private final IProgress progressProvider;

    public GuiEnergyBufferBar(int x, int y, IProgress progressProvider) {
        super(x, y, 8, 62, BAR_TYPE);
        this.progressProvider = progressProvider;
    }

    // ====---------------------------------------------------------------------------====
    // region Getters

    @Nullable
    @Override
    public IProgress getProgressProvider() {
        return progressProvider;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Tooltips

    @Override
    public void addTooltips(List<Component> tooltips) {
        super.addTooltips(tooltips);

        // TODO: Localize
        tooltips.add(new TextComponent(
                StringFormatting.formatNumber(progressProvider.getProgressCurrent())
                        + "/"
                        + StringFormatting.formatNumber(progressProvider.getProgressMax()) + " FE")
        );
    }

    // endregion
}
