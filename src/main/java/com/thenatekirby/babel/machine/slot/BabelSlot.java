package com.thenatekirby.babel.machine.slot;

import com.thenatekirby.babel.capability.item.ValidatedSlotItemHandler;
import com.thenatekirby.babel.core.container.EmptyContainer;
import com.thenatekirby.babel.gui.GuiView;
import com.thenatekirby.babel.machine.config.InventoryItemSlot;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// ====---------------------------------------------------------------------------====

/**
 * An implementation of Slot that routes all decisions through a BabelItemHandler
 */

public class BabelSlot extends Slot {
    private static final Container EMPTY_INVENTORY = EmptyContainer.INSTANCE;

    private final InventoryItemSlot inventoryItemSlot;

    public BabelSlot(InventoryItemSlot inventoryItemSlot) {
        super(EMPTY_INVENTORY, 0, inventoryItemSlot.getPosX(), inventoryItemSlot.getPosY());
        this.inventoryItemSlot = inventoryItemSlot;
    }

    public InventoryItemSlot getInventoryItemSlot() {
        return inventoryItemSlot;
    }

    @Nullable
    public GuiView getHintView() {
        return inventoryItemSlot.getHintView();
    }

    private ValidatedSlotItemHandler getItemHandler() {
        return inventoryItemSlot.getItemHandler();
    }

    // ====---------------------------------------------------------------------------====
    // Interaction

    @Nonnull
    private ItemStack insertItem(@Nonnull ItemStack stack, boolean simulate) {
        return getItemHandler().insertItem(0, stack, simulate);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemStack) {
        return !itemStack.isEmpty() && insertItem(itemStack, true).getCount() < itemStack.getCount();
    }

    @Override
    @Nonnull
    public ItemStack getItem() {
        return getItemHandler().getStackInSlot(0);
    }

    @Override
    public void set(@Nonnull ItemStack itemStack) {
        getItemHandler().setItemStack(itemStack);
    }

    @Override
    public boolean mayPickup(@Nonnull Player player) {
        return !getItemHandler().extractItem(0, 1, true).isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack remove(int count) {
        return getItemHandler().extractItem(0, count, false);
    }

    @Override
    public void onTake(@Nonnull Player player, @Nonnull ItemStack itemStack) {
        super.onTake(player, itemStack);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        getItemHandler().onSlotChanged();
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return getItemHandler().getSlotLimit(0);
    }
}
