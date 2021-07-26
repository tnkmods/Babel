package com.thenatekirby.babel.core.slots;

import com.thenatekirby.babel.gui.GuiView;
import com.thenatekirby.babel.util.ItemStackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

// An IItemHandler with a built-in validator so that only certain items can go into
// this slot
public class ItemSlot implements IItemHandler {

    @Nonnull
    private ItemStack itemStack = ItemStack.EMPTY;

    private final Predicate<ItemStack> validator;

    private boolean enabled = true;

    private final int posX;
    private final int posY;
    private int maxStackSize = 0;

    private IOnSlotChangedListener listener;
    private GuiView hintView;

    public ItemSlot(int posX, int posY) {
        this(posX, posY, ItemStackPredicates.ALL);
    }

    public ItemSlot(int posX, int posY, Predicate<ItemStack> validator) {
        this.posX = posX;
        this.posY = posY;
        this.validator = validator;
    }

    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setListener(IOnSlotChangedListener listener) {
        this.listener = listener;
    }

    public ItemSlot withMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        return this;
    }

    public ItemSlot withHintView(GuiView hintView) {
        this.hintView = hintView;
        return this;
    }

    public GuiView getHintView() {
        return hintView;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region IItemHandler

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemStack;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack insertStack, boolean simulate) {
        if (!enabled || insertStack.isEmpty() || !validator.test(insertStack)) {
            return insertStack;
        }

        if (itemStack.isEmpty()) {
            int max = insertStack.getMaxStackSize();
            ItemStack stack = ItemStackUtil.itemStackWithSize(insertStack, Math.min(insertStack.getCount(), max));

            if (!simulate) {
                setItemStack(stack);
                onSlotChanged();
            }

            if (insertStack.getCount() <= max) {
                return ItemStack.EMPTY;
            } else {
                return ItemStackUtil.itemStackWithSize(insertStack, insertStack.getCount() - max);
            }
        }

        if (ItemStackUtil.areItemStacksAndTagsEqual(itemStack, insertStack)) {
            int count = itemStack.getCount() + insertStack.getCount();
            int maxCapacity = itemStack.getMaxStackSize();
            if (count <= maxCapacity) {
                if (!simulate) {
                    itemStack.setCount(count);
                    onSlotChanged();
                }

                return ItemStack.EMPTY;
            }

            if (!simulate) {
                itemStack.setCount(maxCapacity);
                onSlotChanged();
            }

            return ItemStackUtil.itemStackWithSize(insertStack, count - maxCapacity);
        }

        return insertStack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!enabled) {
            return ItemStack.EMPTY;
        }

        int extracted = Math.min(itemStack.getCount(), amount);
        ItemStack extractedStack = ItemStackUtil.itemStackWithSize(itemStack, extracted);
        if (!simulate) {
            itemStack.shrink(extracted);
            if (itemStack.isEmpty()) {
                setItemStack(ItemStack.EMPTY);
            }

            onSlotChanged();
        }

        return extractedStack;
    }

    @Override
    public int getSlotLimit(int slot) {
        if (maxStackSize > 0) {
            return maxStackSize;
        }

        return itemStack.getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return enabled && validator.test(stack);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Extensions

    public void setItemStack(@Nonnull ItemStack itemStack) {
        if (this.itemStack.getItem() != itemStack.getItem() || this.itemStack.getCount() != itemStack.getCount() || !this.itemStack.getEnchantmentTagList().equals(itemStack.getEnchantmentTagList())) {
            this.itemStack = itemStack;

            onSlotChanged();
        }
    }

    @Nonnull
    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void onSlotChanged() {
        if (this.listener != null) {
            this.listener.onSlotChanged(this);
        }
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region NBT

    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        itemStack.write(nbt);
        return nbt;
    }

    public void deserializeNBT(@Nonnull CompoundNBT nbt) {
        this.itemStack = ItemStack.read(nbt);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Listener

    public interface IOnSlotChangedListener {
        void onSlotChanged(ItemSlot slot);
    }
}
