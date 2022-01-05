package com.thenatekirby.babel.gui.bars;

import com.thenatekirby.babel.core.progress.IProgress;

import javax.annotation.Nullable;

// ====---------------------------------------------------------------------------====

public class GuiProgressBar extends GuiHorizontalBar {
    private static final IHorizontalBarType BAR_TYPE = new IHorizontalBarType() {
        @Override
        public int getTextureX(boolean filled) {
            return filled ? 32 : 0;
        }

        @Override
        public int getTextureY(boolean filled) {
            return 80;
        }
    };

    private final IProgress progressProvider;

    public GuiProgressBar(int x, int y, IProgress progressProvider) {
        super(x, y, 32, 16, BAR_TYPE);
        this.progressProvider = progressProvider;
    }

    @Nullable
    @Override
    public IProgress getProgressProvider() {
        return progressProvider;
    }
}
