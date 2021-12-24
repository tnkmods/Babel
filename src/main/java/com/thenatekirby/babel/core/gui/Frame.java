package com.thenatekirby.babel.core.gui;

import net.minecraft.client.renderer.Rect2i;

// ====---------------------------------------------------------------------------====

public class Frame {
    public final int x;
    public final int y;
    public final int width;
    public final int height;

    public Frame(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public static Frame ZERO = new Frame(0, 0, 0, 0);

    public Frame offsetBy(int x, int y) {
        return new Frame(this.x + x, this.y + y, width, height);
    }

    public Rect2i toRectangle2d() {
        return new Rect2i(x, y, width, height);
    }
}
