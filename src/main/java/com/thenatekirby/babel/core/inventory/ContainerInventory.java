package com.thenatekirby.babel.core.inventory;

import com.thenatekirby.babel.core.energy.BabelEnergyStorage;
import com.thenatekirby.babel.core.energy.EnergyBuffer;
import com.thenatekirby.babel.core.inventory.InventoryItemHandler;
import com.thenatekirby.babel.core.slots.ItemSlot;
import com.thenatekirby.babel.core.inventory.SlotConfiguration;
import com.thenatekirby.babel.core.tileentity.BabelTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ContainerInventory<T extends BabelTileEntity> implements INBTSerializable<CompoundNBT>, BabelEnergyStorage.IEnergyStorageChangedObserver {
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

        this.energyStorage = new BabelEnergyStorage(configuration.getEnergyBuffer());
        this.inputItemHandler = new InventoryItemHandler(this, configuration.getInputItemSlots(), Collections.emptyList(), Collections.emptyList());
        this.outputItemHandler = new InventoryItemHandler(this, Collections.emptyList(), configuration.getOutputItemSlots(), Collections.emptyList());
        this.allItemHandler = new InventoryItemHandler(this, configuration.getInputItemSlots(), configuration.getOutputItemSlots(), configuration.getAuxilaryItemSlots());

        List<ItemSlot> itemSlots = new ArrayList<>();
        itemSlots.addAll(configuration.getInputItemSlots());
        itemSlots.addAll(configuration.getOutputItemSlots());
        itemSlots.addAll(configuration.getAuxilaryItemSlots());
        this.manualItemHandler = new BabelItemHandler(this, itemSlots);
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

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (energyStorage != null) {
            energyStorage.deserializeNBT(nbt.getCompound("energy"));
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


}
