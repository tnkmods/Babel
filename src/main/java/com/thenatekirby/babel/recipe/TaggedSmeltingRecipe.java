package com.thenatekirby.babel.recipe;

import com.google.gson.JsonObject;
import com.thenatekirby.babel.mod.BabelSerializers;
import com.thenatekirby.babel.core.RecipeIngredient;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// ====---------------------------------------------------------------------------====

/**
 * A FurnaceRecipe whose result can be an item tag (but does not have to be). The *actual*
 * result of the recipe (from getRecipeOutput()) will be determined at runtime by selecting
 * the first available tagged item.
 */

public class TaggedSmeltingRecipe extends FurnaceRecipe {
    public static String RECIPE_TYPE_NAME = "smelting";
    private RecipeIngredient result;

    private TaggedSmeltingRecipe(ResourceLocation recipeId, String group, Ingredient input, RecipeIngredient result, float experience, int cookTime) {
        super(recipeId, group, input, ItemStack.EMPTY, experience, cookTime);
        this.result = result;
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return BabelSerializers.TAGGED_SMELTING.getAsRecipeSerializer();
    }

    @Override
    @Nonnull
    public ItemStack assemble(IInventory inv) {
        return getResultItem();
    }

    @Override
    @Nonnull
    public ItemStack getResultItem() {
        return result.makeItemStack();
    }

    // ====---------------------------------------------------------------------------====
    // region Serializer

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<TaggedSmeltingRecipe> {
        @Override
        @Nonnull
        public TaggedSmeltingRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            String group = JSONUtils.getAsString(json, "group", "");
            Ingredient input = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "ingredient"));

            int experience = JSONUtils.getAsInt(json, "experience");
            int cookingTime = JSONUtils.getAsInt(json, "cookingtime");

            JsonObject resultJson = JSONUtils.getAsJsonObject(json, "result");
            if (resultJson.has("tag")) {
                return new TaggedSmeltingRecipe(recipeId, group, input, RecipeIngredient.fromTag(JSONUtils.getAsString(resultJson, "tag")), experience, cookingTime);
            } else {
                return new TaggedSmeltingRecipe(recipeId, group, input, RecipeIngredient.fromItem(JSONUtils.getAsString(resultJson, "item")), experience, cookingTime);
            }
        }

        @Nullable
        @Override
        public TaggedSmeltingRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
            String group = buffer.readUtf();
            Ingredient input = Ingredient.fromNetwork(buffer);
            RecipeIngredient result = RecipeIngredient.read(buffer);
            float experience = buffer.readFloat();
            int cookTime = buffer.readVarInt();
            return new TaggedSmeltingRecipe(recipeId, group, input, result, experience, cookTime);
        }

        @Override
        public void toNetwork(@Nonnull PacketBuffer buffer, @Nonnull TaggedSmeltingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.ingredient.toNetwork(buffer);
            recipe.result.write(buffer);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookingTime);
        }
    }

    // endregion
}
