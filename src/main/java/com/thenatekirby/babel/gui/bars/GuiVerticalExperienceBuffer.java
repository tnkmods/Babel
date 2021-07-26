package com.thenatekirby.babel.gui.bars;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.api.IProgress;
import com.thenatekirby.babel.core.capability.ExperienceStorage;
import com.thenatekirby.babel.core.gui.GuiRenderer;
import com.thenatekirby.babel.gui.core.Frame;
import com.thenatekirby.babel.network.sync.SyncableExperience;
import com.thenatekirby.babel.util.StringFormatting;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.List;

public class GuiVerticalExperienceBuffer extends GuiVerticalBar {
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

    public GuiVerticalExperienceBuffer(int x, int y, SyncableExperience progressProvider) {
        super(x, y, 8, 62, BAR_TYPE);
        this.progressProvider = progressProvider;
    }

    @Nullable
    @Override
    public IProgress getProgressProvider() {
        return progressProvider.getTotalProgress();
    }

    @Override
    public void addTooltips(List<ITextComponent> tooltips) {
        super.addTooltips(tooltips);

        tooltips.add(new StringTextComponent(StringFormatting.formatNumber(getProgressProvider().getProgressCurrent()) + "/" + StringFormatting.formatNumber(getProgressProvider().getProgressMax()) + " Experience"));
    }
}
