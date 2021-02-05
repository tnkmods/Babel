package com.thenatekirby.babel.core.tileentity;

import com.thenatekirby.babel.core.inventory.ContainerInventory;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BabelTileEntity extends TileEntity {
    protected ContainerInventory inventory;

    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.empty();
    private LazyOptional<IItemHandler> itemHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> manualItemHandler = LazyOptional.empty();

    public BabelTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void setWorldAndPos(@Nonnull World world, BlockPos pos) {
        super.setWorldAndPos(world, pos);
        updateCapabilities();
    }

    protected void updateCapabilities() {
        if (inventory.getEnergyStorage().getCapacity() > 0) {
            energyStorage = LazyOptional.of(() ->  inventory.getEnergyStorage());
        } else {
            energyStorage = LazyOptional.empty();
        }

        itemHandler = LazyOptional.of(() -> inventory.getAllItemHandler());
        manualItemHandler = LazyOptional.of(() -> inventory.getManualItemHandler());
    }

    public ContainerInventory getInventory() {
        return inventory;
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


    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        if (inventory != null && nbt.contains("inv", Constants.NBT.TAG_COMPOUND)) {
            inventory.deserializeNBT(nbt.getCompound("inv"));
        }

        super.read(state, nbt);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {

        if (inventory != null) {
            compound.put("inv", inventory.serializeNBT());
        }

        return super.write(compound);
    }
}
