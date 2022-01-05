package com.thenatekirby.babel.gui.bars;

import com.thenatekirby.babel.core.progress.IProgress;
import com.thenatekirby.babel.core.progress.InvertedProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.Nullable;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class GuiCooldownBar extends GuiHorizontalBar {
    private static final IHorizontalBarType BAR_TYPE = new IHorizontalBarType() {
        @Override
        public int getTextureX(boolean filled) {
            return filled ? 33 : 0;
        }

        @Override
        public int getTextureY(boolean filled) {
            return 173;
        }
    };

    private final IProgress progressProvider;

    public GuiCooldownBar(int x, int y, IProgress progressProvider) {
        super(x, y, 33, 5, BAR_TYPE);
        this.progressProvider = new InvertedProgress(progressProvider);
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
        tooltips.add(new TextComponent("Cooldown: " + progressProvider.getProgressCurrent() + " ticks"));
    }

    // endregion
}
