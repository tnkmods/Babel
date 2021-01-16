package com.thenatekirby.babel.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

@SuppressWarnings("WeakerAccess")
public class ItemUtil {
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
}
