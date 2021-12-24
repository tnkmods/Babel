package com.thenatekirby.babel.machine.entity;

import com.thenatekirby.babel.core.NBTConstants;
import com.thenatekirby.babel.machine.inventory.MachineInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// ====---------------------------------------------------------------------------====

@SuppressWarnings("rawtypes")
public class BabelBlockEntity extends BlockEntity {
    protected MachineInventory inventory;

    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.empty();
    private LazyOptional<IItemHandler> itemHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> manualItemHandler = LazyOptional.empty();

    public BabelBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState blockState) {
        super(tileEntityTypeIn, pos, blockState);
    }

    @Override
    public void setLevel(@Nonnull Level level) {
        super.setLevel(level);
        updateCapabilities();
    }

    protected void updateCapabilities() {
        if (inventory == null) {
            return;
        }

        if (inventory.getEnergyStorage().getCapacity() > 0) {
            energyStorage = LazyOptional.of(() ->  inventory.getEnergyStorage());
        } else {
            energyStorage = LazyOptional.empty();
        }

        itemHandler = LazyOptional.of(() -> inventory.getAllItemHandler());
        manualItemHandler = LazyOptional.of(() -> inventory.getManualItemHandler());
    }

    @Nullable
    public MachineInventory getInventory() {
        return inventory;
    }

    public void markInventoryDirty() {
        if (level != null && !level.isClientSide) {
            onInventoryChanged((ServerLevel) level);
        }
    }

    public void onInventoryChanged(@Nonnull ServerLevel world) {
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
            return manualItemHandler.cast();
        }

        return super.getCapability(cap);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (inventory == null) {
            return super.getCapability(cap, side);
        }

        if (cap.equals(CapabilityEnergy.ENERGY)) {
            return energyStorage.cast();
        }

        if (cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
            if (side == null) {
                return manualItemHandler.cast();
            }

            return itemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    // ====---------------------------------------------------------------------------====
    // region NBT

    @Override
    public void load(@Nonnull CompoundTag compoundTag) {
        if (inventory != null && compoundTag.contains("inv", NBTConstants.TAG_COMPOUND)) {
            inventory.deserializeNBT(compoundTag.getCompound("inv"));
        }

        super.load(compoundTag);
    }

    @Override
    @Nonnull
    public CompoundTag save(@Nonnull CompoundTag compound) {
        if (inventory != null) {
            compound.put("inv", inventory.serializeNBT());
        }

        return super.save(compound);
    }

    // endregion
}
