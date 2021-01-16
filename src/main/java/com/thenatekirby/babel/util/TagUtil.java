package com.thenatekirby.babel.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@SuppressWarnings("WeakerAccess")
public class TagUtil {
    public static boolean doesItemTagExist(@Nonnull ResourceLocation id) {
        ITag<Item> itemTag = ItemTags.getCollection().get(id);
        return (itemTag != null);
    }

    public static boolean doesBlockTagExist(@Nonnull ResourceLocation id) {
        ITag<Block> blockTag = BlockTags.getCollection().get(id);
        return (blockTag != null);
    }

    public static ItemStack firstItemInTag(@Nonnull ResourceLocation id) {
        return firstItemInTag(id, 1);
    }

    public static ItemStack firstItemInTag(@Nonnull ResourceLocation id, int count) {
        ITag<Item> itemTag = ItemTags.getCollection().get(id);
        if (itemTag == null || itemTag.getAllElements().isEmpty()) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(itemTag.getAllElements().get(0), count);
    }
}
