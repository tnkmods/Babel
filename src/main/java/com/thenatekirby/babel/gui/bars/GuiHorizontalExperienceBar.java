package com.thenatekirby.babel.gui.bars;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thenatekirby.babel.capability.experience.ExperienceStorage;
import com.thenatekirby.babel.core.api.IGuiRenderer;
import com.thenatekirby.babel.core.gui.Frame;
import com.thenatekirby.babel.core.progress.IProgress;
import com.thenatekirby.babel.network.sync.SyncableExperience;

import javax.annotation.Nullable;

// ====---------------------------------------------------------------------------====

public class GuiHorizontalExperienceBar extends GuiHorizontalBar {
    private static final IHorizontalBarType BAR_TYPE = new IHorizontalBarType() {
        @Override
        public int getTextureX(boolean filled) {
            return filled ? 64 : 0;
        }

        @Override
        public int getTextureY(boolean filled) {
            return 165;
        }
    };

    private SyncableExperience syncableExperience;

    public GuiHorizontalExperienceBar(int x, int y, SyncableExperience syncableExperience) {
        super(x, y, 64, 8, BAR_TYPE);
        this.syncableExperience = syncableExperience;
    }

    @Nullable
    @Override
    public IProgress getProgressProvider() {
        return syncableExperience.getLevelProgress();
    }

    @Override
    public void drawFrame(Frame frame, PoseStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
        super.drawFrame(frame, matrixStack, renderer, mouseX, mouseY, partialTicks);

        drawTexturedRect(matrixStack, frame.x, frame.y + 10, 16, 148, 9, 9);

        ExperienceStorage experienceStorage = syncableExperience.getExperienceStorage();
        renderer.getFont().drawShadow(matrixStack, "" + experienceStorage.getExperienceLevel() + "", (float)frame.x + 12f, (float)frame.y + 11f, 8453920);
    }
}
