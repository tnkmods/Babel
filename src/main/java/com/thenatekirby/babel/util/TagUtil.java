package com.thenatekirby.babel.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings({"WeakerAccess", "unused"})
public class TagUtil {
    public static boolean doesItemTagExist(@Nonnull ResourceLocation id) {
        ITag<Item> itemTag = ItemTags.getAllTags().getTag(id);
        return (itemTag != null);
    }

    public static boolean doesBlockTagExist(@Nonnull ResourceLocation id) {
        ITag<Block> blockTag = BlockTags.getAllTags().getTag(id);
        return (blockTag != null);
    }

    @Nullable
    public static ITag<Item> getItemTag(@Nonnull ResourceLocation id) {
        return ItemTags.getAllTags().getTag(id);
    }

    public static ItemStack firstItemInTag(@Nonnull ResourceLocation id) {
        return firstItemInTag(id, 1);
    }

    public static ItemStack firstItemInTag(@Nonnull ResourceLocation id, int count) {
        ITag<Item> itemTag = ItemTags.getAllTags().getTag(id);
        if (itemTag == null || itemTag.getValues().isEmpty()) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(itemTag.getValues().get(0), count);
    }
}
