package com.thenatekirby.babel.core.slots;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;

import java.util.function.Predicate;

public class ItemStackValidators {
    public static Predicate<ItemStack> ALL = (stack) -> true;
    public static Predicate<ItemStack> NONE = (stack) -> false;

    public static Predicate<ItemStack> matchesItem(IItemProvider itemProvider) {
        return (stack) -> itemProvider.asItem().equals(stack.getItem());
    }
}
