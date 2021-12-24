package com.thenatekirby.babel.recipe;

import com.google.gson.JsonObject;
import com.thenatekirby.babel.babelmod.BabelSerializers;
import com.thenatekirby.babel.recipe.components.RecipeIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// ====---------------------------------------------------------------------------====

/**
 * A FurnaceRecipe whose result can be an item tag (but does not have to be). The *actual*
 * result of the recipe (from getRecipeOutput()) will be determined at runtime by selecting
 * the first available tagged item.
 */

public class TaggedSmeltingRecipe extends SmeltingRecipe {
    public static String RECIPE_TYPE_NAME = "smelting";
    private RecipeIngredient result;

    private TaggedSmeltingRecipe(ResourceLocation recipeId, String group, Ingredient input, RecipeIngredient result, float experience, int cookTime) {
        super(recipeId, group, input, ItemStack.EMPTY, experience, cookTime);
        this.result = result;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return BabelSerializers.TAGGED_SMELTING.getAsRecipeSerializer();
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull Container inv) {
        return getResultItem();
    }

    @Override
    @Nonnull
    public ItemStack getResultItem() {
        return result.makeItemStack();
    }

    // ====---------------------------------------------------------------------------====
    // region Serializer

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<TaggedSmeltingRecipe> {
        @Override
        @Nonnull
        public TaggedSmeltingRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));

            int experience = GsonHelper.getAsInt(json, "experience");
            int cookingTime = GsonHelper.getAsInt(json, "cookingtime");

            JsonObject resultJson = GsonHelper.getAsJsonObject(json, "result");
            if (resultJson.has("tag")) {
                return new TaggedSmeltingRecipe(recipeId, group, input, RecipeIngredient.fromTag(GsonHelper.getAsString(resultJson, "tag")), experience, cookingTime);
            } else {
                return new TaggedSmeltingRecipe(recipeId, group, input, RecipeIngredient.fromItem(GsonHelper.getAsString(resultJson, "item")), experience, cookingTime);
            }
        }

        @Override
        @Nullable
        public TaggedSmeltingRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Ingredient input = Ingredient.fromNetwork(buffer);
            RecipeIngredient result = RecipeIngredient.read(buffer);
            float experience = buffer.readFloat();
            int cookTime = buffer.readVarInt();
            return new TaggedSmeltingRecipe(recipeId, group, input, result, experience, cookTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, TaggedSmeltingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.ingredient.toNetwork(buffer);
            recipe.result.write(buffer);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookingTime);
        }
    }

    // endregion
}
