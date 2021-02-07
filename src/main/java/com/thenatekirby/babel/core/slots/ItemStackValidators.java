package com.thenatekirby.babel.core.slots;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;

import java.util.function.Predicate;

public class ItemStackValidators {
    public static Predicate<ItemStack> ALL = (stack) -> true;
    public static Predicate<ItemStack> NONE = (stack) -> false;

    public static Predicate<ItemStack> IS_ENCHANTED = ItemStack::isEnchanted;
    public static Predicate<ItemStack> IS_ENCHANTED_BOOK = (stack) -> stack.getItem() == Items.BOOK && stack.isEnchanted();
    public static Predicate<ItemStack> IS_ENCHANTED_NON_BOOK = (stack) -> stack.getItem() != Items.BOOK && stack.isEnchanted();

    public static Predicate<ItemStack> matchesItem(IItemProvider itemProvider) {
        return (stack) -> itemProvider.asItem().equals(stack.getItem());
    }
}
