package com.thenatekirby.babel.core.inventory;

import com.thenatekirby.babel.core.slots.ItemSlot;
import com.thenatekirby.babel.util.ItemStackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class InventoryItemHandler implements IItemHandler {
    @Nonnull
    private final ContainerInventory inventory;

    @Nonnull
    private final List<ItemSlot> inputSlots;

    @Nonnull
    private final List<ItemSlot> outputSlots;

    @Nonnull
    private final List<ItemSlot> auxiliarySlots;

    @Nonnull
    private final List<ItemSlot> allSlots = new ArrayList<>();

    public InventoryItemHandler(@Nonnull ContainerInventory inventory, @Nonnull List<ItemSlot> inputSlots, @Nonnull List<ItemSlot> outputSlots, @Nonnull List<ItemSlot> auxiliarySlots) {
        this.inventory = inventory;
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        this.auxiliarySlots = auxiliarySlots;

        this.allSlots.addAll(inputSlots);
        this.allSlots.addAll(outputSlots);
        this.allSlots.addAll(auxiliarySlots);
    }

    @Override
    public int getSlots() {
        return allSlots.size();
    }

    public ItemSlot getSlot(int slot) {
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
        int inputSlotCount = inputSlots.size();
        if (slot < 0 || slot > inputSlotCount) {
            return stack;
        }

        ItemStack insertStack = ItemStackUtil.itemStackWithSize(stack, stack.getCount());
        for (ItemSlot itemSlot : inputSlots) {
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
        int inputSlotCount = inputSlots.size();
        if (slot < inputSlotCount || slot > getSlots()) {
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
    public List<ItemSlot> getInputSlots() {
        return inputSlots;
    }

    @Nonnull
    public List<ItemSlot> getOutputSlots() {
        return outputSlots;
    }

    @Nonnull
    public List<ItemSlot> getAuxiliarySlots() {
        return auxiliarySlots;
    }

    @Nonnull
    public List<ItemSlot> getAllSlots() {
        return allSlots;
    }

    // ====---------------------------------------------------------------------------====
    // region NBT

    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT listNBT = new ListNBT();

        for (ItemSlot slot : allSlots) {
            listNBT.add(slot.serializeNBT());
        }

        nbt.put("slots", listNBT);
        return nbt;
    }

    public void deserializeNBT(@Nonnull CompoundNBT nbt) {
        ListNBT listNBT = nbt.getList("slots", Constants.NBT.TAG_COMPOUND);
        for (int idx = 0; idx < listNBT.size(); idx++) {
            CompoundNBT slotNBT = listNBT.getCompound(idx);
            allSlots.get(idx).deserializeNBT(slotNBT);
        }
    }

    // endregion
}
