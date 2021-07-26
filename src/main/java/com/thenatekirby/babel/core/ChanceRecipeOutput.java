package com.thenatekirby.babel.core;

import com.google.gson.JsonObject;
import com.thenatekirby.babel.util.ItemStackUtil;
import com.thenatekirby.babel.util.TagUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class ChanceRecipeOutput {
    private String resultId;

    private int count;

    private float chance;

    private ChanceRecipeOutput(String resultId, int count, float chance) {
        this.resultId = resultId;
        this.count = count;
        this.chance = chance;
    }

    public static ChanceRecipeOutput fromItem(@Nonnull String resultId, float chance) {
        return new ChanceRecipeOutput(resultId, 1, chance);
    }

    public static ChanceRecipeOutput fromItem(@Nonnull String resultId, int count, float chance) {
        return new ChanceRecipeOutput(resultId, count, chance);
    }

    public static ChanceRecipeOutput fromItem(@Nonnull IItemProvider item, float chance) {
        return new ChanceRecipeOutput(item.asItem().getRegistryName().toString(), 1, chance);
    }

    public static ChanceRecipeOutput fromItem(@Nonnull IItemProvider item, int count, float chance) {
        return new ChanceRecipeOutput(item.asItem().getRegistryName().toString(), count, chance);
    }

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

    public void write(@Nonnull PacketBuffer buffer) {
        buffer.writeString(resultId);
        buffer.writeInt(count);
        buffer.writeFloat(chance);
    }

    public static ChanceRecipeOutput read(@Nonnull PacketBuffer buffer) {
        String resultId = buffer.readString();
        int count = buffer.readInt();
        float chance = buffer.readFloat();
        return new ChanceRecipeOutput(resultId, count, chance);
    }
}
