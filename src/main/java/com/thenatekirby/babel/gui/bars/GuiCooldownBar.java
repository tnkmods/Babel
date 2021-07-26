package com.thenatekirby.babel.gui.bars;

import com.thenatekirby.babel.api.IProgress;
import com.thenatekirby.babel.core.InvertedProgress;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.List;

import com.thenatekirby.babel.gui.bars.GuiHorizontalBar.IHorizontalBarType;

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

    private IProgress progressProvider;

    public GuiCooldownBar(int x, int y, IProgress progressProvider) {
        super(x, y, 33, 5, BAR_TYPE);
        this.progressProvider = new InvertedProgress(progressProvider);
    }

    @Nullable
    @Override
    public IProgress getProgressProvider() {
        return progressProvider;
    }

    @Override
    public void addTooltips(List<ITextComponent> tooltips) {
        super.addTooltips(tooltips);
        tooltips.add(new StringTextComponent("Cooldown: " + progressProvider.getProgressCurrent() + " ticks"));
    }
}
