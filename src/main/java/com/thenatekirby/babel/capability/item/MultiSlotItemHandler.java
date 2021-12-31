package com.thenatekirby.babel.capability.item;

import com.thenatekirby.babel.machine.config.InventoryItemSlot;
import com.thenatekirby.babel.util.ItemStackUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

// ====---------------------------------------------------------------------------====

/**
 * An implementation of IItemHandler that handles multiple slots correctly, deferring
 * each to a SlotItemHandler, so they can validate their own contents
 */
public class MultiSlotItemHandler implements IItemHandler {
    @Nonnull
    private final List<ValidatedSlotItemHandler> allSlots;

    public MultiSlotItemHandler(@Nonnull List<InventoryItemSlot> allSlots) {
        List<ValidatedSlotItemHandler> slotItemHandlers = new ArrayList();
        for (InventoryItemSlot slot : allSlots) {
            slotItemHandlers.add(slot.getItemHandler());
        }

        this.allSlots = slotItemHandlers;
    }

    @Override
    public int getSlots() {
        return allSlots.size();
    }

    public ValidatedSlotItemHandler getSlot(int slot) {
        return allSlots.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return getSlot(slot).getItemStack();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot < 0 || slot > getSlots()) {
            return stack;
        }

        ItemStack insertStack = ItemStackUtil.itemStackWithSize(stack, stack.getCount());
        for (ValidatedSlotItemHandler itemSlot : allSlots) {
            insertStack = itemSlot.insertItem(0, insertStack, simulate);

            if (insertStack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }

        return insertStack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot < 0 || slot > getSlots()) {
            return ItemStack.EMPTY;
        }

        return allSlots.get(slot).extractItem(0, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return getSlot(slot).getSlotLimit(0);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return getSlot(slot).isItemValid(0, stack);
    }

    @Nonnull
    public List<ValidatedSlotItemHandler> getAllSlots() {
        return allSlots;
    }
}
