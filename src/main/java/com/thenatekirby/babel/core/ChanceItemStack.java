package com.thenatekirby.babel.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;

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

    public void write(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeItem(itemStack);
        buffer.writeFloat(chance);
    }

    public static ChanceItemStack read(@Nonnull FriendlyByteBuf buffer) {
        ItemStack itemStack = buffer.readItem();
        float chance = buffer.readFloat();
        return new ChanceItemStack(itemStack, chance);
    }

    @Nullable
    public static ChanceItemStack from(JsonObject json) {
        ItemStack itemStack;
        float chance;

        if (GsonHelper.isValidNode(json, "item")) {
            itemStack = ShapedRecipe.itemStackFromJson(json);

        } else {
            return null;
        }

        if (GsonHelper.isValidNode(json, "chance")) {
            chance = GsonHelper.getAsFloat(json, "chance");

        } else {
            chance = 1.0f;
        }

        return new ChanceItemStack(itemStack, chance);
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = itemStack.serializeNBT();
        nbt.putFloat("chance", chance);
        return nbt;
    }

    public JsonElement serializeJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("item", itemStack.getItem().getRegistryName().toString());

        if (itemStack.getCount() > 1) {
            jsonObject.addProperty("count", itemStack.getCount());
        }

        jsonObject.addProperty("chance", chance);
        return jsonObject;
    }

    public JsonElement serializeItemStackJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("item", itemStack.getItem().getRegistryName().toString());

        if (itemStack.getCount() > 1) {
            jsonObject.addProperty("count", itemStack.getCount());
        }

        return jsonObject;
    }

    // endregion
}