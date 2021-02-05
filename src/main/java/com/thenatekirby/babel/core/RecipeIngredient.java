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

public class RecipeIngredient {
    private boolean isTag;

    private String resultId;

    private int count;

    private RecipeIngredient(boolean isTag, String resultId, int count) {
        this.isTag = isTag;
        this.resultId = resultId;
        this.count = count;
    }

    public static RecipeIngredient fromTag(@Nonnull String resultId) {
        return new RecipeIngredient(true, resultId, 1);
    }

    public static RecipeIngredient fromTag(@Nonnull String resultId, int count) {
        return new RecipeIngredient(true, resultId, count);
    }

    public static RecipeIngredient fromTag(@Nonnull ResourceLocation resultId) {
        return new RecipeIngredient(true, resultId.toString(), 1);
    }

    public static RecipeIngredient fromItem(@Nonnull String resultId) {
        return new RecipeIngredient(false, resultId, 1);
    }

    public static RecipeIngredient fromItem(@Nonnull String resultId, int count) {
        return new RecipeIngredient(false, resultId, count);
    }

    public static RecipeIngredient fromItem(@Nonnull ResourceLocation resultId) {
        return new RecipeIngredient(false, resultId.toString(), 1);
    }

    public static RecipeIngredient fromItem(@Nonnull IItemProvider item) {
        return new RecipeIngredient(false, item.asItem().getRegistryName().toString(), 1);
    }

    @Nonnull
    public JsonObject serializeJson() {
        JsonObject jsonObject = new JsonObject();
        if (isTag) {
            jsonObject.addProperty("tag", this.resultId.toString());
        } else {
            jsonObject.addProperty("item", this.resultId.toString());
        }

        if (count > 1) {
            jsonObject.addProperty("count", this.count);
        }

        return jsonObject;
    }

    @Nonnull
    public ItemStack makeItemStack() {
        ResourceLocation id = new ResourceLocation(resultId);

        if (this.isTag) {
            return TagUtil.firstItemInTag(id, count);

        } else {
            return ItemStackUtil.makeItemStack(id, count);
        }
    }

    public void write(@Nonnull PacketBuffer buffer) {
        buffer.writeBoolean(isTag);
        buffer.writeString(resultId);
        buffer.writeInt(count);
    }

    public static RecipeIngredient read(@Nonnull PacketBuffer buffer) {
        boolean isTag = buffer.readBoolean();
        String resultId = buffer.readString();
        int count = buffer.readInt();
        return new RecipeIngredient(isTag, resultId, count);
    }

    public Ingredient makeIngredient() {
        if (this.isTag) {
            return Ingredient.fromTag(TagUtil.getItemTag(new ResourceLocation(this.resultId)));
        } else {
            return Ingredient.fromItems(ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.resultId)));
        }
    }
}
