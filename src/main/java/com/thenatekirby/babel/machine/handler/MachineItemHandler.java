package com.thenatekirby.babel.machine.handler;

import com.thenatekirby.babel.capability.item.ValidatedSlotItemHandler;
import com.thenatekirby.babel.core.NBTConstants;
import com.thenatekirby.babel.machine.config.InventoryItemSlot;
import com.thenatekirby.babel.machine.inventory.DeviceInventory;
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
    private final DeviceInventory inventory;

    @Nonnull
    private final List<InventoryItemSlot> inputSlots;

    @Nonnull
    private final List<InventoryItemSlot> outputSlots;

    @Nonnull
    private final List<InventoryItemSlot> auxiliarySlots;

    @Nonnull
    private final List<InventoryItemSlot> allSlots = new ArrayList<>();

    public MachineItemHandler(@Nonnull DeviceInventory inventory, @Nonnull List<InventoryItemSlot> inputSlots, @Nonnull List<InventoryItemSlot> outputSlots, @Nonnull List<InventoryItemSlot> auxiliarySlots) {
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

    public InventoryItemSlot getSlot(int slot) {
        return allSlots.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return getSlot(slot).getItemHandler().getItemStack();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        int inputSlotCount = inputSlots.size();
        if (slot < 0 || slot > inputSlotCount) {
            return stack;
        }

        ItemStack insertStack = ItemStackUtil.itemStackWithSize(stack, stack.getCount());
        for (InventoryItemSlot itemSlot : inputSlots) {
            insertStack = itemSlot.getItemHandler().insertItem(0, insertStack, simulate);

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

        return allSlots.get(slot).getItemHandler().extractItem(0, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return getSlot(slot).getItemHandler().getSlotLimit(0);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return getSlot(slot).getItemHandler().isItemValid(0, stack);
    }

    @Nonnull
    public List<InventoryItemSlot> getInputSlots() {
        return inputSlots;
    }

    @Nonnull
    public List<InventoryItemSlot> getOutputSlots() {
        return outputSlots;
    }

    @Nonnull
    public List<InventoryItemSlot> getAuxiliarySlots() {
        return auxiliarySlots;
    }

    @Nonnull
    public List<InventoryItemSlot> getAllSlots() {
        return allSlots;
    }

    // ====---------------------------------------------------------------------------====
    // region NBT

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag listNBT = new ListTag();

        for (InventoryItemSlot slot : allSlots) {
            listNBT.add(slot.getItemHandler().serializeNBT());
        }

        nbt.put("slots", listNBT);
        return nbt;
    }

    public void deserializeNBT(@Nonnull CompoundTag nbt) {
        ListTag listNBT = nbt.getList("slots", NBTConstants.TAG_COMPOUND);
        for (int idx = 0; idx < listNBT.size(); idx++) {
            CompoundTag slotNBT = listNBT.getCompound(idx);
            allSlots.get(idx).getItemHandler().deserializeNBT(slotNBT);
        }
    }

    // endregion
}
