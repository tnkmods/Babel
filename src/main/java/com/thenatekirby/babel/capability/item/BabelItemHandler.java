package com.thenatekirby.babel.capability.item;

import com.thenatekirby.babel.util.ItemStackUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

// ====---------------------------------------------------------------------------====

/**
 * An implementation of IItemHandler that handles multiple slots correctly, deferring
 * each to a SlotItemHandler, so they can validate their own contents
 */
public class BabelItemHandler implements IItemHandler {
    @Nonnull
    private final List<BabelSlotItemHandler> allSlots;

    public BabelItemHandler(@Nonnull List<BabelSlotItemHandler> allSlots) {
        this.allSlots = allSlots;
    }

    @Override
    public int getSlots() {
        return allSlots.size();
    }

    public BabelSlotItemHandler getSlot(int slot) {
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
        for (BabelSlotItemHandler itemSlot : allSlots) {
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
    public List<BabelSlotItemHandler> getAllSlots() {
        return allSlots;
    }
}
