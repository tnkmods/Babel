package com.thenatekirby.babel.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// ====---------------------------------------------------------------------------====

public class TagUtil {
    public static boolean doesItemTagExist(@Nonnull ResourceLocation id) {
        Tag<Item> itemTag = ItemTags.getAllTags().getTag(id);
        return (itemTag != null);
    }

    public static boolean doesBlockTagExist(@Nonnull ResourceLocation id) {
        Tag<Block> blockTag = BlockTags.getAllTags().getTag(id);
        return (blockTag != null);
    }

    @Nullable
    public static Tag<Item> getItemTag(@Nonnull ResourceLocation id) {
        return ItemTags.getAllTags().getTag(id);
    }

    @Nonnull
    public static ItemStack firstItemInTag(@Nonnull ResourceLocation id) {
        return firstItemInTag(id, 1);
    }

    @Nonnull
    public static ItemStack firstItemInTag(@Nonnull ResourceLocation id, int count) {
        Tag<Item> itemTag = ItemTags.getAllTags().getTag(id);
        if (itemTag == null || itemTag.getValues().isEmpty()) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(itemTag.getValues().get(0), count);
    }
}
