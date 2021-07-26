package com.thenatekirby.babel.gui.core;

public class ClickContext {
    public final double mouseX;
    public final double mouseY;
    public final int buttonIndex;
    public final boolean holdingShift;

    public ClickContext(double mouseX, double mouseY, int buttonIndex, boolean holdingShift) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.buttonIndex = buttonIndex;
        this.holdingShift = holdingShift;
    }

}
