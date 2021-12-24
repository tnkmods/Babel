package com.thenatekirby.babel.gui;

import com.thenatekirby.babel.core.api.IBooleanProvider;
import com.thenatekirby.babel.core.api.IEnergyStatsProvider;
import com.thenatekirby.babel.core.api.IRedstoneModeProvider;
import com.thenatekirby.babel.core.progress.IProgress;
import com.thenatekirby.babel.gui.bars.*;
import com.thenatekirby.babel.gui.buttons.GuiButton;
import com.thenatekirby.babel.gui.buttons.GuiRedstoneToggleButton;
import com.thenatekirby.babel.gui.buttons.GuiToggleWorkingAreaButton;
import com.thenatekirby.babel.gui.render.GuiRenderer;
import com.thenatekirby.babel.gui.views.GuiEnergyStats;
import com.thenatekirby.babel.gui.views.GuiTabView;
import com.thenatekirby.babel.network.sync.SyncableExperience;

import java.util.List;

// ====---------------------------------------------------------------------------====

public class ViewBuilder {
    private GuiRenderer renderer;
    private final List<GuiView> subviews;

    public ViewBuilder(GuiRenderer renderer, List<GuiView> subviews) {
        this.renderer = renderer;
        this.subviews = subviews;
    }

    public ViewBuilder with(GuiView view) {
        subviews.add(view);
        return this;
    }

    public ViewBuilder withRightTabAtOffset(int y) {
        return with(new GuiTabView(GuiTabView.TabType.RIGHT, renderer.getGuiWidth(), y));
    }

    public ViewBuilder withRedstoneToggle(int x, int y, IRedstoneModeProvider provider, GuiRedstoneToggleButton.IOnRedstoneToggledListener listener) {
        return with(new GuiRedstoneToggleButton(x, y, provider, listener));
    }

    public ViewBuilder withRedstoneTabAtOffset(int y, IRedstoneModeProvider provider, GuiRedstoneToggleButton.IOnRedstoneToggledListener listener) {
        with(new GuiTabView(GuiTabView.TabType.RIGHT, renderer.getGuiWidth(), y));
        with(new GuiRedstoneToggleButton(renderer.getGuiWidth() + 3, y + 4, provider, listener));
        return this;
    }

    public ViewBuilder withEnergyBuffer(int x, int y, IProgress progress) {
        return with(new GuiEnergyBufferBar(x, y, progress));
    }

    public ViewBuilder withLeftLargeTabAtOffset(int y) {
        return with(new GuiTabView(GuiTabView.TabType.LEFT_LARGE, -25, y));
    }

    public ViewBuilder withExperienceBuffer(int x, int y, SyncableExperience syncableExperience) {
        return with(new GuiVerticalExperienceBar(x, y, syncableExperience));
    }

    public ViewBuilder withHorizontalExperienceBuffer(int x, int y, SyncableExperience syncableExperience) {
        return with(new GuiHorizontalExperienceBar(x, y, syncableExperience));
    }

    public ViewBuilder withProgressBar(int x, int y, IProgress progress) {
        return with(new GuiProgressBar(x, y, progress));
    }

    public ViewBuilder withEnergyStats(int x, int y, IEnergyStatsProvider provider) {
        return with(new GuiEnergyStats(x, y, provider));
    }

    public ViewBuilder withEnergyStatsTabAtOffset(int y, IEnergyStatsProvider provider) {
        with(new GuiTabView(GuiTabView.TabType.RIGHT, renderer.getGuiWidth(), y));
        with(new GuiEnergyStats(renderer.getGuiWidth() + 4, y + 5, provider));
        return this;
    }

    public ViewBuilder withCooldownBar(int x, int y, IProgress progress) {
        return with(new GuiCooldownBar(x, y, progress));
    }

    public ViewBuilder withToggleWorkingAreaTabAtOffset(int y, IBooleanProvider provider, GuiButton.IOnClickListener onClickListener) {
        with(new GuiTabView(GuiTabView.TabType.RIGHT, renderer.getGuiWidth(), y));
        with(new GuiToggleWorkingAreaButton(renderer.getGuiWidth() + 3, y + 4, provider, onClickListener));
        return this;
    }

    public List<GuiView> getSubviews() {
        return subviews;
    }
}
