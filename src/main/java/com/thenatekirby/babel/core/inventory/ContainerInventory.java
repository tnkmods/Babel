package com.thenatekirby.babel.core.inventory;

import com.thenatekirby.babel.core.energy.BabelEnergyStorage;
import com.thenatekirby.babel.core.energy.EnergyBuffer;
import com.thenatekirby.babel.core.slots.ItemSlot;
import com.thenatekirby.babel.core.tileentity.BabelTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        this.tileEntity.markDirty();
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region IOnSlotChangedListener


    @Override
    public void onSlotChanged(ItemSlot slot) {
        this.tileEntity.markDirty();
        this.tileEntity.markInventoryDirty();
    }
}
