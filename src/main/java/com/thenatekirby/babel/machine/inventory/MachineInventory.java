package com.thenatekirby.babel.machine.inventory;


import com.thenatekirby.babel.capability.energy.BabelEnergyStorage;
import com.thenatekirby.babel.capability.item.BabelItemHandler;
import com.thenatekirby.babel.capability.item.BabelSlotItemHandler;
import com.thenatekirby.babel.machine.config.EnergyBuffer;
import com.thenatekirby.babel.machine.config.SlotConfiguration;
import com.thenatekirby.babel.machine.entity.BabelBlockEntity;
import com.thenatekirby.babel.machine.handler.MachineItemHandler;
import com.thenatekirby.babel.util.ItemStackUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// ====---------------------------------------------------------------------------====

@SuppressWarnings("rawtypes")
public abstract class MachineInventory<T extends BabelBlockEntity> implements INBTSerializable<CompoundTag>, BabelEnergyStorage.IEnergyStorageChangedObserver, BabelSlotItemHandler.IOnSlotChangedListener {
    private final T tileEntity;

    private BabelEnergyStorage energyStorage = BabelEnergyStorage.fromBuffer(EnergyBuffer.ZERO);
    private MachineItemHandler inputItemHandler;
    private MachineItemHandler outputItemHandler;
    private MachineItemHandler allItemHandler;
    private BabelItemHandler manualItemHandler;

    public MachineInventory(T tileEntity) {
        this.tileEntity = tileEntity;

        this.setupHandlers();
    }

    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    public BabelEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public MachineItemHandler getInputItemHandler() {
        return inputItemHandler;
    }

    public MachineItemHandler getOutputItemHandler() {
        return outputItemHandler;
    }

    public MachineItemHandler getAllItemHandler() {
        return allItemHandler;
    }

    public BabelItemHandler getManualItemHandler() {
        return manualItemHandler;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Setup

    public abstract SlotConfiguration makeConfiguration();

    private void setupHandlers() {
        SlotConfiguration configuration = makeConfiguration();
        setupHandlers(configuration);
    }

    @SuppressWarnings("unchecked")
    protected void setupHandlers(@Nonnull SlotConfiguration configuration) {
        List<BabelSlotItemHandler> inputSlots = configuration.getInputItemSlots();
        List<BabelSlotItemHandler> outputSlots = configuration.getOutputItemSlots();
        List<BabelSlotItemHandler> auxSlots = configuration.getAuxilaryItemSlots();

        List<BabelSlotItemHandler> allSlots = new ArrayList<>();
        allSlots.addAll(inputSlots);
        allSlots.addAll(outputSlots);
        allSlots.addAll(auxSlots);

        this.energyStorage = BabelEnergyStorage.fromBuffer(configuration.getEnergyBuffer());
        this.energyStorage.setEnergyChangedObserver(this);

        this.inputItemHandler = new MachineItemHandler(this, inputSlots, Collections.emptyList(), Collections.emptyList());
        this.outputItemHandler = new MachineItemHandler(this, Collections.emptyList(), outputSlots, Collections.emptyList());
        this.allItemHandler = new MachineItemHandler(this, inputSlots, outputSlots, auxSlots);
        this.manualItemHandler = new BabelItemHandler(allSlots);

        for (BabelSlotItemHandler slot : allSlots) {
            slot.setListener(this);
        }
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Helpers

    public int getSlotCount() {
        return allItemHandler.getSlots();
    }

    // Helper for BabelContainer.transferStackInSlot only.
    public boolean canInsert(@Nonnull ItemStack itemStack) {
        for (BabelSlotItemHandler itemSlot : inputItemHandler.getInputSlots()) {
            ItemStack result = itemSlot.insertItem(0, itemStack, true);
            if (result.getCount() < itemStack.getCount()) {
                return true;
            }
        }

        return false;
    }

    public boolean isSlotEmpty(int index) {
        return getStackInSlot(index).isEmpty();
    }

    public void emptySlot(int index) {
        setStackInSlot(ItemStack.EMPTY, index);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Items

    public List<BabelSlotItemHandler> getInputSlots() {
        return allItemHandler.getInputSlots();
    }

    public List<BabelSlotItemHandler> getOutputSlots() {
        return allItemHandler.getOutputSlots();
    }

    public List<BabelSlotItemHandler> getAuxiliarySlots() {
        return allItemHandler.getAuxiliarySlots();
    }

    public ItemStack getStackInSlot(int index) {
        return allItemHandler.getSlot(index).getItemStack();
    }

    public void setStackInSlot(@Nonnull ItemStack itemStack, int index) {
        allItemHandler.getSlot(index).setItemStack(itemStack);
    }

    public ItemStack getAuxiliaryStackInSlot(int index) {
        return getAuxiliarySlots().get(index).getItemStack();
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Contents

    public MachineContents getInventoryContents() {
        List<ItemStack> inputs = getInputSlots().stream().map(BabelSlotItemHandler::getItemStack).collect(Collectors.toList());
        return new MachineContents(inputs, Collections.emptyList());
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region I/O

    public boolean canOutput(ItemStack outputStack) {
        ItemStack resultStack = ItemStackUtil.copyItemStack(outputStack);
        if (resultStack.isEmpty()) {
            return false;
        }

        for (BabelSlotItemHandler itemSlot : getOutputSlots()) {
            ItemStack slotStack = itemSlot.getItemStack();
            if (slotStack.isEmpty()) {
                return true;
            }

            if (ItemStackUtil.areItemStacksEqual(resultStack, slotStack)) {
                int nextCount = slotStack.getCount() + resultStack.getCount();
                if (nextCount <= resultStack.getMaxStackSize()) {
                    return true;
                }

                resultStack.shrink(slotStack.getMaxStackSize() - slotStack.getCount());
            }
        }

        return false;
    }

    public void transferToOutput(ItemStack itemStack) {
        ItemStack resultStack = ItemStackUtil.copyItemStack(itemStack);
        if (resultStack.isEmpty()) {
            return;
        }

        for (BabelSlotItemHandler itemSlot : getOutputSlots()) {
            ItemStack slotStack = itemSlot.getItemStack();
            if (slotStack.isEmpty()) {
                itemSlot.setItemStack(resultStack);
                return;
            }

            if (ItemStackUtil.areItemStacksEqual(resultStack, slotStack)) {
                int nextCount = slotStack.getCount() + resultStack.getCount();
                if (nextCount <= slotStack.getMaxStackSize()) {
                    slotStack.setCount(nextCount);
                    return;
                }

                resultStack.shrink(slotStack.getMaxStackSize() - slotStack.getCount());
            }
        }
    }

    public void transferToOutput(ItemStack itemStack, int slot) {
        allItemHandler.getSlot(slot).setItemStack(itemStack);
    }

    public void consumeInputInSlot(int slot) {
        consumeInputInSlot(slot, 1);
    }

    public void consumeInputInSlot(int slot, int count) {
        if (getStackInSlot(slot).getCount() <= count) {
            setStackInSlot(ItemStack.EMPTY, slot);
        } else {
            getStackInSlot(slot).shrink(count);
        }
    }

    public void consumeInputs(List<ItemStack> itemStacks) {
        for (BabelSlotItemHandler itemSlot : getInputSlots()) {
            for (ItemStack itemStack : itemStacks) {
                if (ItemStackUtil.areItemStacksEqual(itemSlot.getItemStack(), itemStack)) {
                    if (itemSlot.getItemStack().getCount() <= itemStack.getCount()) {
                        itemSlot.setItemStack(ItemStack.EMPTY);
                    } else {
                        itemSlot.getItemStack().shrink(itemStack.getCount());
                    }
                }
            }
        }
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region NBT

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        if (energyStorage != null) {
            nbt.put("energy", energyStorage.serializeNBT());
        }

        if (allItemHandler != null) {
            nbt.put("items", allItemHandler.serializeNBT());
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (energyStorage != null) {
            energyStorage.deserializeNBT(nbt.getCompound("energy"));
        }

        if (allItemHandler != null) {
            allItemHandler.deserializeNBT(nbt.getCompound("items"));
        }
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region IEnergyStorageChangedObserver

    @Override
    public void onEnergyChanged(BabelEnergyStorage storage) {
        this.tileEntity.setChanged();
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region IOnSlotChangedListener

    @Override
    public void onSlotChanged(BabelSlotItemHandler slot) {
        this.tileEntity.setChanged();
        this.tileEntity.markInventoryDirty();
    }
}
