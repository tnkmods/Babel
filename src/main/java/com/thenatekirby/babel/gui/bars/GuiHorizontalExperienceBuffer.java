package com.thenatekirby.babel.gui.bars;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.api.IGuiRenderer;
import com.thenatekirby.babel.api.IProgress;
import com.thenatekirby.babel.core.capability.ExperienceStorage;
import com.thenatekirby.babel.core.gui.GuiRenderer;
import com.thenatekirby.babel.gui.core.Frame;
import com.thenatekirby.babel.network.sync.SyncableExperience;
import net.minecraft.client.gui.screen.EnchantmentScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.List;

public class GuiHorizontalExperienceBuffer extends GuiHorizontalBar {
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

    public GuiHorizontalExperienceBuffer(int x, int y, SyncableExperience syncableExperience) {
        super(x, y, 64, 8, BAR_TYPE);
        this.syncableExperience = syncableExperience;
    }

    @Nullable
    @Override
    public IProgress getProgressProvider() {
        return syncableExperience.getLevelProgress();
    }

    @Override
    public void drawFrame(Frame frame, MatrixStack matrixStack, IGuiRenderer renderer, int mouseX, int mouseY, float partialTicks) {
        super.drawFrame(frame, matrixStack, renderer, mouseX, mouseY, partialTicks);

        drawTexturedRect(matrixStack, frame.x, frame.y + 10, 16, 148, 9, 9);

        ExperienceStorage experienceStorage = syncableExperience.getExperienceStorage();
        renderer.getFontRenderer().drawStringWithShadow(matrixStack, "" + experienceStorage.getExperienceLevel() + "", (float)frame.x + 12f, (float)frame.y + 11f, 8453920);
    }

    @Override
    public void addTooltips(List<ITextComponent> tooltips) {
        super.addTooltips(tooltips);
    }
}
