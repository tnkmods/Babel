package com.thenatekirby.babel.machine.slot;

import com.thenatekirby.babel.capability.item.BabelSlotItemHandler;
import com.thenatekirby.babel.core.container.EmptyContainer;
import com.thenatekirby.babel.gui.GuiView;
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
    private static Container EMPTY_INVENTORY = EmptyContainer.INSTANCE;

    private final BabelSlotItemHandler itemHandler;

    public BabelSlot(BabelSlotItemHandler itemSlot) {
        super(EMPTY_INVENTORY, 0, itemSlot.getPosX(), itemSlot.getPosY());
        this.itemHandler = itemSlot;
    }

    public BabelSlotItemHandler getItemHandler() {
        return itemHandler;
    }

    @Nullable
    public GuiView getHintView() {
        return itemHandler.getHintView();
    }

    // ====---------------------------------------------------------------------------====
    // Interaction

    @Nonnull
    private ItemStack insertItem(@Nonnull ItemStack stack, boolean simulate) {
        return itemHandler.insertItem(0, stack, simulate);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemStack) {
        return !itemStack.isEmpty() && insertItem(itemStack, true).getCount() < itemStack.getCount();
    }

    @Override
    @Nonnull
    public ItemStack getItem() {
        return itemHandler.getStackInSlot(0);
    }

    @Override
    public void set(@Nonnull ItemStack itemStack) {
        itemHandler.setItemStack(itemStack);
    }

    @Override
    public boolean mayPickup(@Nonnull Player player) {
        return !itemHandler.extractItem(0, 1, true).isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack remove(int count) {
        return itemHandler.extractItem(0, count, false);
    }

    @Override
    public void onTake(@Nonnull Player player, @Nonnull ItemStack itemStack) {
        super.onTake(player, itemStack);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        itemHandler.onSlotChanged();
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return itemHandler.getSlotLimit(0);
    }
}
