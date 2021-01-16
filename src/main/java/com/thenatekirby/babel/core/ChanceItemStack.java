package com.thenatekirby.babel.core;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// ====---------------------------------------------------------------------------====

/**
 * An ItemStack wrapper with an associated chance, for chance-based recipe outputs.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class ChanceItemStack {
    @Nonnull
    private final ItemStack itemStack;
    private final float chance;

    public ChanceItemStack(@Nonnull ItemStack itemStack, float chance) {
        this.itemStack = itemStack;
        this.chance = chance;
    }

    // ====---------------------------------------------------------------------------====
    // region Getters

    @Nonnull
    public ItemStack getItemStack() {
        return itemStack;
    }

    public float getChance() {
        return chance;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Serialization

    public void write(@Nonnull PacketBuffer buffer) {
        buffer.writeItemStack(itemStack);
        buffer.writeFloat(chance);
    }

    public static ChanceItemStack read(@Nonnull PacketBuffer buffer) {
        ItemStack itemStack = buffer.readItemStack();
        float chance = buffer.readFloat();
        return new ChanceItemStack(itemStack, chance);
    }

    @Nullable
    public static ChanceItemStack from(JsonObject json) {
        ItemStack itemStack;
        float chance;

        if (JSONUtils.hasField(json, "item")) {
            itemStack = ShapedRecipe.deserializeItem(json);

        } else {
            return null;
        }

        if (JSONUtils.hasField(json, "chance")) {
            chance = JSONUtils.getFloat(json, "chance");

        } else {
            chance = 1.0f;
        }

        return new ChanceItemStack(itemStack, chance);
    }

    // endregion
}