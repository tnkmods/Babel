package com.thenatekirby.babel.core.slots;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TieredItem;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.extensions.IForgeItemStack;

import java.util.function.Predicate;

public class ItemStackPredicates {
    public static Predicate<ItemStack> ALL = (stack) -> true;
    public static Predicate<ItemStack> NONE = (stack) -> false;

    public static Predicate<ItemStack> IS_ENCHANTED = (stack) -> stack.isEnchanted() || stack.getItem() == Items.ENCHANTED_BOOK;
    public static Predicate<ItemStack> IS_ENCHANTED_BOOK = (stack) -> stack.getItem() == Items.ENCHANTED_BOOK;
    public static Predicate<ItemStack> IS_ENCHANTED_NON_BOOK = (stack) -> stack.getItem() != Items.ENCHANTED_BOOK && stack.isEnchanted();
    public static Predicate<ItemStack> BOOK_WTIH_MULTIPLE_ENCHANTS = (stack) -> stack.getItem() == Items.ENCHANTED_BOOK && EnchantmentHelper.getEnchantments(stack).size() > 1;

    public static Predicate<ItemStack> IS_TIERED = (stack) -> stack.getItem() instanceof TieredItem;
    public static Predicate<ItemStack> IS_ARMOR = (stack) -> stack.getItem() instanceof ArmorItem;
    public static Predicate<ItemStack> IS_REPAIRABLE = IForgeItemStack::isRepairable;
    public static Predicate<ItemStack> IS_REPAIRABLE_WITH_DAMAGE = (stack) -> stack.isRepairable() && stack.getDamageValue() > 0;

    public static Predicate<ItemStack> matchesItem(IItemProvider itemProvider) {
        return (stack) -> itemProvider.asItem().equals(stack.getItem());
    }


}
