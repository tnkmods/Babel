package com.thenatekirby.babel.core.slots;

import com.thenatekirby.babel.gui.GuiView;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BabelSlot extends Slot {
    private static IInventory EMPTY_INVENTORY = new Inventory(0);

    private final ItemSlot inventory;

    public BabelSlot(ItemSlot itemSlot) {
        super(EMPTY_INVENTORY, 0, itemSlot.getPosX(), itemSlot.getPosY());
        this.inventory = itemSlot;
    }

    public ItemSlot getInventory() {
        return inventory;
    }

    @Nullable
    public GuiView getHintView() {
        return inventory.getHintView();
    }

    // ====---------------------------------------------------------------------------====
    // Interaction

    @Nonnull
    private ItemStack insertItem(@Nonnull ItemStack stack, boolean simulate) {
        return inventory.insertItem(0, stack, simulate);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemStack) {
        return !itemStack.isEmpty() && insertItem(itemStack, true).getCount() < itemStack.getCount();
    }

    @Override
    @Nonnull
    public ItemStack getItem() {
        return inventory.getStackInSlot(0);
    }

    @Override
    public void set(@Nonnull ItemStack itemStack) {
        inventory.setItemStack(itemStack);
    }

    @Override
    public boolean mayPickup(PlayerEntity entity) {
        return !inventory.extractItem(0, 1, true).isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack remove(int count) {
        return inventory.extractItem(0, count, false);
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        return super.onTake(thePlayer, stack);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        inventory.onSlotChanged();
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return inventory.getSlotLimit(0);
    }
}
