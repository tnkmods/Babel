package com.thenatekirby.babel.machine.handler;

import com.thenatekirby.babel.capability.item.BabelSlotItemHandler;
import com.thenatekirby.babel.core.NBTConstants;
import com.thenatekirby.babel.machine.inventory.MachineInventory;
import com.thenatekirby.babel.util.ItemStackUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class MachineItemHandler implements IItemHandler {
    @Nonnull
    private final MachineInventory inventory;

    @Nonnull
    private final List<BabelSlotItemHandler> inputSlots;

    @Nonnull
    private final List<BabelSlotItemHandler> outputSlots;

    @Nonnull
    private final List<BabelSlotItemHandler> auxiliarySlots;

    @Nonnull
    private final List<BabelSlotItemHandler> allSlots = new ArrayList<>();

    public MachineItemHandler(@Nonnull MachineInventory inventory, @Nonnull List<BabelSlotItemHandler> inputSlots, @Nonnull List<BabelSlotItemHandler> outputSlots, @Nonnull List<BabelSlotItemHandler> auxiliarySlots) {
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
        int inputSlotCount = inputSlots.size();
        if (slot < 0 || slot > inputSlotCount) {
            return stack;
        }

        ItemStack insertStack = ItemStackUtil.itemStackWithSize(stack, stack.getCount());
        for (BabelSlotItemHandler itemSlot : inputSlots) {
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
        int maxOutputSlot = inputSlotCount + outputSlots.size();
        if (slot < inputSlotCount || slot >= maxOutputSlot) {
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
    public List<BabelSlotItemHandler> getInputSlots() {
        return inputSlots;
    }

    @Nonnull
    public List<BabelSlotItemHandler> getOutputSlots() {
        return outputSlots;
    }

    @Nonnull
    public List<BabelSlotItemHandler> getAuxiliarySlots() {
        return auxiliarySlots;
    }

    @Nonnull
    public List<BabelSlotItemHandler> getAllSlots() {
        return allSlots;
    }

    // ====---------------------------------------------------------------------------====
    // region NBT

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag listNBT = new ListTag();

        for (BabelSlotItemHandler slot : allSlots) {
            listNBT.add(slot.serializeNBT());
        }

        nbt.put("slots", listNBT);
        return nbt;
    }

    public void deserializeNBT(@Nonnull CompoundTag nbt) {
        ListTag listNBT = nbt.getList("slots", NBTConstants.TAG_COMPOUND);
        for (int idx = 0; idx < listNBT.size(); idx++) {
            CompoundTag slotNBT = listNBT.getCompound(idx);
            allSlots.get(idx).deserializeNBT(slotNBT);
        }
    }

    // endregion
}
