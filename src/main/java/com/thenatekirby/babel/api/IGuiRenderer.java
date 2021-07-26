package com.thenatekirby.babel.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thenatekirby.babel.gui.core.Frame;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface IGuiRenderer {

    // region Size & Frame
    int getGuiLeft();
    int getGuiTop();
    int getGuiWidth();
    int getGuiHeight();

    // endregion
    // region Textures
    void bindTexture(@Nonnull ResourceLocation resourceLocation);

    // endregion
    // region Rendering

    // endregion
    // region Helpers

    @Nonnull
    FontRenderer getFontRenderer();
}
