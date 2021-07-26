package com.thenatekirby.babel.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

@SuppressWarnings("WeakerAccess")
public class ItemStackUtil {
    public static ItemStack makeItemStack(@Nonnull ResourceLocation id) {
        return makeItemStack(id, 1);
    }

    public static ItemStack makeItemStack(@Nonnull ResourceLocation id, int count) {
        Item item = ForgeRegistries.ITEMS.getValue(id);
        if (item != null) {
            return new ItemStack(item, count);
        } else {
            return ItemStack.EMPTY;
        }
    }

    public static ItemStack itemStackWithSize(@Nonnull ItemStack itemStack, int size) {
        if (itemStack.isEmpty() || size <= 0) {
            return ItemStack.EMPTY;
        }

        ItemStack nextStack = itemStack.copy();
        nextStack.setCount(size);
        return nextStack;
    }

    public static ItemStack copyItemStack(@Nonnull ItemStack itemStack) {
        return itemStackWithSize(itemStack, itemStack.getCount());
    }

    public static boolean areItemStacksEqual(@Nonnull ItemStack lhs, @Nonnull ItemStack rhs) {
        return ItemStack.isSame(lhs, rhs);
    }

    public static boolean areItemStackTagsEqual(@Nonnull ItemStack lhs, @Nonnull ItemStack rhs) {
        return ItemStack.tagMatches(lhs, rhs);
    }

    public static boolean areItemStacksAndTagsEqual(@Nonnull ItemStack lhs, @Nonnull ItemStack rhs) {
        return areItemStacksEqual(lhs, rhs) && areItemStackTagsEqual(lhs, rhs);
    }

    public static void stripEnchantments(@Nonnull ItemStack itemStack) {
        itemStack.removeTagKey("Enchantments");
        itemStack.removeTagKey("StoredEnchantments");
    }
}
