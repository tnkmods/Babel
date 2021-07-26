package com.thenatekirby.babel.gui.bars;

import com.thenatekirby.babel.api.IProgress;
import com.thenatekirby.babel.util.StringFormatting;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.List;

public class GuiEnergyBuffer extends GuiVerticalBar {
    private static IVerticalBarType BAR_TYPE = new IVerticalBarType() {
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

    public GuiEnergyBuffer(int x, int y, IProgress progressProvider) {
        super(x, y, 8, 62, BAR_TYPE);
        this.progressProvider = progressProvider;
    }

    @Nullable
    @Override
    public IProgress getProgressProvider() {
        return progressProvider;
    }

    @Override
    public void addTooltips(List<ITextComponent> tooltips) {
        super.addTooltips(tooltips);

        tooltips.add(new StringTextComponent(StringFormatting.formatNumber(progressProvider.getProgressCurrent()) + "/" + StringFormatting.formatNumber(progressProvider.getProgressMax()) + " FE"));
    }
}
