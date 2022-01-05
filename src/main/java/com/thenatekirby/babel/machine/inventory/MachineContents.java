package com.thenatekirby.babel.machine.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class MachineContents {
    @Nonnull
    private final List<ItemStack> items;

    @Nonnull
    private final List<FluidStack> fluids;

    public MachineContents(@Nonnull List<ItemStack> items, @Nonnull List<FluidStack> fluids) {
        this.items = items;
        this.fluids = fluids;
    }

    // ====---------------------------------------------------------------------------====
    // region Getters

    @Nonnull
    public List<ItemStack> getItems() {
        return items;
    }

    @Nonnull
    public List<FluidStack> getFluids() {
        return fluids;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Matching

    public boolean matches(@Nonnull MachineContents containerContents) {
        for (ItemStack itemStack : containerContents.items) {
            if (!hasItemStack(itemStack)) {
                return false;
            }
        }

        // TODO: Fluids

        return true;
    }

    public boolean hasItemStack(@Nonnull ItemStack otherStack) {
        for (ItemStack item : items) {
            if (item.getItem() == otherStack.getItem() && item.getCount() >= otherStack.getCount()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasFluidStack(@Nonnull FluidStack otherStack) {
        // TODO: Fluids
        return false;
    }

    // endregion
}
