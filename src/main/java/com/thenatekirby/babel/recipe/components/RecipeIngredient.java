package com.thenatekirby.babel.recipe.components;

import com.google.gson.JsonObject;
import com.thenatekirby.babel.core.api.IItemProvider;
import com.thenatekirby.babel.util.ItemStackUtil;
import com.thenatekirby.babel.util.TagUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

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

    public static RecipeIngredient fromItem(@Nonnull ItemLike item) {
        return new RecipeIngredient(false, item.asItem().getRegistryName().toString(), 1);
    }

    public static RecipeIngredient fromItem(@Nonnull ItemLike item, int count) {
        return new RecipeIngredient(false, item.asItem().getRegistryName().toString(), count);
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

    public void write(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeBoolean(isTag);
        buffer.writeUtf(resultId);
        buffer.writeInt(count);
    }

    public static RecipeIngredient read(@Nonnull FriendlyByteBuf buffer) {
        boolean isTag = buffer.readBoolean();
        String resultId = buffer.readUtf();
        int count = buffer.readInt();
        return new RecipeIngredient(isTag, resultId, count);
    }

    public Ingredient makeIngredient() {
        if (this.isTag) {
            return Ingredient.of(TagUtil.getItemTag(new ResourceLocation(this.resultId)));
        } else {
            return Ingredient.of(ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.resultId)));
        }
    }
}
