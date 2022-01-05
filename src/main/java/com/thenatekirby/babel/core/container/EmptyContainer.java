package com.thenatekirby.babel.core.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class EmptyContainer implements Container {
    public static EmptyContainer INSTANCE = new EmptyContainer();

    private EmptyContainer() {
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
    public boolean stillValid(@Nonnull Player player) {
        return false;
    }

    @Override
    public void clearContent() {

    }

}