package com.thenatekirby.babel.gui.slot;

import com.thenatekirby.babel.gui.core.GuiTextureView;

public class GuiSlotTexture extends GuiTextureView {
    private static final IGuiTexture TEXTURE = new IGuiTexture() {
        @Override
        public int getTextureX() {
            return 0;
        }

        @Override
        public int getTextureY() {
            return 0;
        }
    };

    public GuiSlotTexture(int x, int y) {
        super(x, y, 18, 18, TEXTURE);
    }
}
