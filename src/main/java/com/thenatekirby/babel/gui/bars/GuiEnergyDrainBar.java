package com.thenatekirby.babel.gui.bars;

import com.thenatekirby.babel.core.progress.ConstantProgress;
import com.thenatekirby.babel.core.progress.IProgress;

import javax.annotation.Nullable;

// ====---------------------------------------------------------------------------====

public class GuiEnergyDrainBar extends GuiVerticalBar {
    private static final IVerticalBarType BAR_TYPE = new IVerticalBarType() {
        @Override
        public int getTextureX(boolean filled) {
            return filled ? 8 : 0;
        }

        @Override
        public int getTextureY(boolean filled) {
            return 178;
        }
    };

    private final IProgress progressProvider;

    public GuiEnergyDrainBar(int x, int y) {
        super(x, y, 8, 44, BAR_TYPE);
        this.progressProvider = new ConstantProgress(100);
    }

    @Nullable
    @Override
    public IProgress getProgressProvider() {
        return progressProvider;
    }
}
