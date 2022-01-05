package com.thenatekirby.babel.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.drawable.IDrawable;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class EmptyJEIBackground implements IDrawable {
    private final int width;
    private final int height;

    public EmptyJEIBackground(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void draw(@Nonnull PoseStack matrixStack, int i, int i1) {
    }
}