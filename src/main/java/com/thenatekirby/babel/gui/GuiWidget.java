package com.thenatekirby.babel.gui;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class GuiWidget extends Widget {
    public static final ITextComponent NO_MESSAGE_COMPONENT = new StringTextComponent("");

    public GuiWidget(int x, int y, int width, int height, ITextComponent message) {
        super(x, y, width, height, message);
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.visible && mouseX >= (double)this.x && mouseY >= (double)this.y && mouseX < (double)(this.x + this.width) && mouseY < (double)(this.y + this.height);
    }
}
