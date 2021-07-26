package com.thenatekirby.babel.core.inventory;

import com.thenatekirby.babel.core.energy.BabelEnergyStorage;
import com.thenatekirby.babel.core.energy.EnergyBuffer;
import com.thenatekirby.babel.core.slots.ItemSlot;
import com.thenatekirby.babel.core.tileentity.BabelTileEntity;
import com.thenatekirby.babel.util.ItemStackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ContainerInventory<T extends BabelTileEntity> implements INBTSerializable<CompoundNBT>, BabelEnergyStorage.IEnergyStorageChangedObserver, ItemSlot.IOnSlotChangedListener {
    private final T tileEntity;

    private BabelEnergyStorage energyStorage = new BabelEnergyStorage(EnergyBuffer.ZERO);
    private InventoryItemHandler inputItemHandler;
    private InventoryItemHandler outputItemHandler;
    private InventoryItemHandler allItemHandler;
    private BabelItemHandler manualItemHandler;

    public ContainerInventory(T tileEntity) {
        this.tileEntity = tileEntity;

        this.setupHandlers();
    }

    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    public BabelEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public InventoryItemHandler getInputItemHandler() {
        return inputItemHandler;
    }

    public InventoryItemHandler getOutputItemHandler() {
        return outputItemHandler;
    }

    public InventoryItemHandler getAllItemHandler() {
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
        List<ItemSlot> inputSlots = configuration.getInputItemSlots();
        List<ItemSlot> outputSlots = configuration.getOutputItemSlots();
        List<ItemSlot> auxSlots = configuration.getAuxilaryItemSlots();

        List<ItemSlot> allSlots = new ArrayList<>();
        allSlots.addAll(inputSlots);
        allSlots.addAll(outputSlots);
        allSlots.addAll(auxSlots);

        this.energyStorage = new BabelEnergyStorage(configuration.getEnergyBuffer());
        this.energyStorage.setEnergyChangedObserver(this);

        this.inputItemHandler = new InventoryItemHandler(this, inputSlots, Collections.emptyList(), Collections.emptyList());
        this.outputItemHandler = new InventoryItemHandler(this, Collections.emptyList(), outputSlots, Collections.emptyList());
        this.allItemHandler = new InventoryItemHandler(this, inputSlots, outputSlots, auxSlots);
        this.manualItemHandler = new BabelItemHandler(allSlots);

        for (ItemSlot slot : allSlots) {
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
        for (ItemSlot itemSlot : inputItemHandler.getInputSlots()) {
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

    public List<ItemSlot> getInputSlots() {
        return allItemHandler.getInputSlots();
    }

    public List<ItemSlot> getOutputSlots() {
        return allItemHandler.getOutputSlots();
    }

    public List<ItemSlot> getAuxiliarySlots() {
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

    public InventoryContents getInventoryContents() {
        List<ItemStack> inputs = getInputSlots().stream().map(ItemSlot::getItemStack).collect(Collectors.toList());
        return new InventoryContents(inputs, Collections.emptyList());
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region I/O

    public boolean canOutput(ItemStack outputStack) {
        ItemStack resultStack = ItemStackUtil.copyItemStack(outputStack);
        if (resultStack.isEmpty()) {
            return false;
        }

        for (ItemSlot itemSlot : getOutputSlots()) {
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

        for (ItemSlot itemSlot : getOutputSlots()) {
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
        for (ItemSlot itemSlot : getInputSlots()) {
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
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        if (energyStorage != null) {
            nbt.put("energy", energyStorage.serializeNBT());
        }

        if (allItemHandler != null) {
            nbt.put("items", allItemHandler.serializeNBT());
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
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
    public void onSlotChanged(ItemSlot slot) {
        this.tileEntity.setChanged();
        this.tileEntity.markInventoryDirty();
    }
}
