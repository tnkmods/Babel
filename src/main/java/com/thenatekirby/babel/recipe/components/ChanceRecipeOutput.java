package com.thenatekirby.babel.recipe.components;

import com.google.gson.JsonObject;
import com.thenatekirby.babel.core.api.IItemProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class ChanceRecipeOutput {
    private String resultId;
    private int count;
    private float chance;

    private ChanceRecipeOutput(String resultId, int count, float chance) {
        this.resultId = resultId;
        this.count = count;
        this.chance = chance;
    }

    // ====---------------------------------------------------------------------------====
    // region Factories

    public static ChanceRecipeOutput fromItem(@Nonnull String resultId, float chance) {
        return new ChanceRecipeOutput(resultId, 1, chance);
    }

    public static ChanceRecipeOutput fromItem(@Nonnull String resultId, int count, float chance) {
        return new ChanceRecipeOutput(resultId, count, chance);
    }

    public static ChanceRecipeOutput fromItem(@Nonnull ItemLike item, float chance) {
        return new ChanceRecipeOutput(item.asItem().getRegistryName().toString(), 1, chance);
    }

    public static ChanceRecipeOutput fromItem(@Nonnull ItemLike item, int count, float chance) {
        return new ChanceRecipeOutput(item.asItem().getRegistryName().toString(), count, chance);
    }

    public static ChanceRecipeOutput fromItem(@Nonnull IItemProvider item, float chance) {
        return new ChanceRecipeOutput(item.asItem().getRegistryName().toString(), 1, chance);
    }

    public static ChanceRecipeOutput fromItem(@Nonnull IItemProvider item, int count, float chance) {
        return new ChanceRecipeOutput(item.asItem().getRegistryName().toString(), count, chance);
    }


    // endregion
    // ====---------------------------------------------------------------------------====
    // region Serialization

    @Nonnull
    public JsonObject serializeJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("item", this.resultId.toString());

        if (count > 1) {
            jsonObject.addProperty("count", this.count);
        }

        if (chance < 1.0) {
            jsonObject.addProperty("chance", this.chance);
        }

        return jsonObject;
    }

    public void write(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeUtf(resultId);
        buffer.writeInt(count);
        buffer.writeFloat(chance);
    }

    public static ChanceRecipeOutput read(@Nonnull FriendlyByteBuf buffer) {
        String resultId = buffer.readUtf();
        int count = buffer.readInt();
        float chance = buffer.readFloat();
        return new ChanceRecipeOutput(resultId, count, chance);
    }

    // endregion
}
