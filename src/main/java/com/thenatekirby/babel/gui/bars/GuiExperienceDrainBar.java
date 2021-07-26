package com.thenatekirby.babel.gui.bars;

import com.thenatekirby.babel.api.IProgress;
import com.thenatekirby.babel.core.ConstantProgress;

import javax.annotation.Nullable;

public class GuiExperienceDrainBar extends GuiVerticalBar {
    private static final IVerticalBarType BAR_TYPE = new IVerticalBarType() {
        @Override
        public int getTextureX(boolean filled) {
            return filled ? 16 : 0;
        }

        @Override
        public int getTextureY(boolean filled) {
            return 178;
        }
    };

    private IProgress progressProvider;

    public GuiExperienceDrainBar(int x, int y) {
        super(x, y, 8, 44, BAR_TYPE);
        this.progressProvider = new ConstantProgress(100);
    }

    @Nullable
    @Override
    public IProgress getProgressProvider() {
        return progressProvider;
    }

    public void setExperiencePercent(int percent) {
        this.progressProvider = new ConstantProgress(percent);
    }
}
