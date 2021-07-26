package com.thenatekirby.babel.core.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class InventoryContents {
    @Nonnull
    private final List<ItemStack> items;

    @Nonnull
    private final List<FluidStack> fluids;

    public InventoryContents(@Nonnull List<ItemStack> items, @Nonnull List<FluidStack> fluids) {
        this.items = items;
        this.fluids = fluids;
    }

    @Nonnull
    public List<ItemStack> getItems() {
        return items;
    }

    @Nonnull
    public List<FluidStack> getFluids() {
        return fluids;
    }

    public boolean matches(@Nonnull InventoryContents inventoryContents) {
        for (ItemStack itemStack : inventoryContents.items) {
            if (!hasItemStack(itemStack)) {
                return false;
            }
        }

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
}
