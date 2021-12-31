package com.thenatekirby.babel.machine.entity;

import com.thenatekirby.babel.core.NBTConstants;
import com.thenatekirby.babel.machine.inventory.DeviceInventory;
import com.thenatekirby.babel.util.ServerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
    protected DeviceInventory inventory;

    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.empty();
    private LazyOptional<IItemHandler> itemHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> manualItemHandler = LazyOptional.empty();

    public BabelBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    // ====---------------------------------------------------------------------------====
    // region Getters

    @Nullable
    public DeviceInventory getInventory() {
        return inventory;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Lifecycle

    @Override
    public void setLevel(@Nonnull Level level) {
        super.setLevel(level);
        updateCapabilities();
        ServerUtil.ifServer(this, this::onInventoryChanged);
    }

    public void markInventoryDirty() {
        ServerUtil.ifServer(this, this::onInventoryChanged);
    }

    public void onInventoryChanged(@Nonnull ServerLevel world) {
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Capabilities

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

    // endregion
    // ====---------------------------------------------------------------------------====
    // region NBT

    @Override
    protected void saveAdditional(@Nonnull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        serializeBlockEntity(compoundTag);
    }

    @Override
    public void load(@Nonnull CompoundTag compoundTag) {
        loadBlockEntity(compoundTag);
        super.load(compoundTag);
    }


    protected void serializeBlockEntity(CompoundTag compoundTag) {
        if (inventory != null) {
            compoundTag.put("inv", inventory.serializeNBT());
        }
    }

    protected void loadBlockEntity(@Nonnull CompoundTag compoundTag) {
        if (inventory != null && compoundTag.contains("inv", NBTConstants.TAG_COMPOUND)) {
            inventory.deserializeNBT(compoundTag.getCompound("inv"));
        }
    }

    // endregion
}
