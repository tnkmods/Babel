package com.thenatekirby.babel.core.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

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

    // ====---------------------------------------------------------------------------====
    // Interaction

    @Nonnull
    private ItemStack insertItem(@Nonnull ItemStack stack, boolean simulate) {
        ItemStack results = inventory.insertItem(0, stack, simulate);
        return results;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack itemStack) {
        return !itemStack.isEmpty() && insertItem(itemStack, true).getCount() < itemStack.getCount();
    }

    @Override
    @Nonnull
    public ItemStack getStack() {
        return inventory.getStackInSlot(0);
    }

    @Override
    public void putStack(@Nonnull ItemStack itemStack) {
        inventory.setItemStack(itemStack);
    }

    @Override
    public boolean canTakeStack(PlayerEntity entity) {
        return !inventory.extractItem(0, 1, true).isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int count) {
        return inventory.extractItem(0, count, false);
    }

}
