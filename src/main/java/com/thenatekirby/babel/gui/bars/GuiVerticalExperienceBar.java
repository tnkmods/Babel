package com.thenatekirby.babel.gui.bars;

import com.thenatekirby.babel.core.progress.IProgress;
import com.thenatekirby.babel.network.sync.SyncableExperience;
import com.thenatekirby.babel.util.StringFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.Nullable;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class GuiVerticalExperienceBar extends GuiVerticalBar {
    private static IVerticalBarType BAR_TYPE = new IVerticalBarType() {
        @Override
        public int getTextureX(boolean filled) {
            return filled ? 16 : 0;
        }

        @Override
        public int getTextureY(boolean filled) {
            return 18;
        }
    };

    private final SyncableExperience progressProvider;

    public GuiVerticalExperienceBar(int x, int y, SyncableExperience progressProvider) {
        super(x, y, 8, 62, BAR_TYPE);
        this.progressProvider = progressProvider;
    }

    @Nullable
    @Override
    public IProgress getProgressProvider() {
        return progressProvider.getTotalProgress();
    }

    @Override
    public void addTooltips(List<Component> tooltips) {
        super.addTooltips(tooltips);

        IProgress progress = getProgressProvider();
        if (progress == null) {
            return;
        }

        tooltips.add(new TextComponent(
                StringFormatting.formatNumber(progress.getProgressCurrent())
                        + "/"
                        + StringFormatting.formatNumber(progress.getProgressMax()) + " Experience")
        );
    }
}
