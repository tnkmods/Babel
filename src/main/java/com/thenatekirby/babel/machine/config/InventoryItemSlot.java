package com.thenatekirby.babel.machine.config;

import com.thenatekirby.babel.capability.item.ValidatedSlotItemHandler;
import com.thenatekirby.babel.gui.GuiView;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

// ====---------------------------------------------------------------------------====

public class InventoryItemSlot {
    private final int posX;
    private final int posY;

    private final ValidatedSlotItemHandler itemHandler;

    private GuiView hintView;

    public InventoryItemSlot(int posX, int posY, Predicate<ItemStack> validator) {
        this.posX = posX;
        this.posY = posY;
        this.itemHandler = new ValidatedSlotItemHandler(validator);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public ValidatedSlotItemHandler getItemHandler() {
        return itemHandler;
    }

    public InventoryItemSlot withHintView(GuiView hintView) {
        this.hintView = hintView;
        return this;
    }

    public GuiView getHintView() {
        return hintView;
    }

    public InventoryItemSlot withMaxStackSize(int stackSize) {
        itemHandler.withMaxStackSize(stackSize);
        return this;
    }

    public ItemStack getItemStack() {
        return itemHandler.getItemStack();
    }
}
