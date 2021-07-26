package com.thenatekirby.babel.core;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class EmptyInventory implements IInventory {
    public static EmptyInventory INSTANCE = new EmptyInventory();

    private EmptyInventory() {
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getItem(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack removeItem(int i, int i1) {
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack removeItemNoUpdate(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, @Nonnull ItemStack itemStack) {

    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity playerEntity) {
        return false;
    }

    @Override
    public void clearContent() {

    }

}