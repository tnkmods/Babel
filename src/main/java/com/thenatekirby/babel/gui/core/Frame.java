package com.thenatekirby.babel.gui.core;

import net.minecraft.client.renderer.Rectangle2d;

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

    public Rectangle2d toRectangle2d() {
        return new Rectangle2d(x, y, width, height);
    }
}
