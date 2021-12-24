package com.thenatekirby.babel.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thenatekirby.babel.babelmod.BabelSerializers;
import com.thenatekirby.babel.recipe.components.RecipeIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// ====---------------------------------------------------------------------------====

/**
 * A ShapelessRecipe whose result can be a tag (but does not have to be). The *actual*
 * result of the recipe (from getRecipeOutput()) will be determined at runtime by selecting
 * the first available tagged item.
 */

public class TaggedShapelessCraftingRecipe extends ShapelessRecipe {
    public static String RECIPE_TYPE_NAME = "crafting_shapeless";

    private final RecipeIngredient result;

    private TaggedShapelessCraftingRecipe(ResourceLocation recipeId, String group, RecipeIngredient result, NonNullList<Ingredient> ingredients) {
        super(recipeId, group, ItemStack.EMPTY, ingredients);
        this.result = result;
    }

    @Override
    @Nonnull
    public ItemStack getResultItem() {
        return result.makeItemStack();
    }

    @Override
    @Nonnull
    public ItemStack assemble(CraftingContainer inv) {
        return getResultItem().copy();
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return BabelSerializers.TAGGED_SHAPELESS.getAsRecipeSerializer();
    }

    // ====---------------------------------------------------------------------------====
    // region Serializer

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<TaggedShapelessCraftingRecipe> {
        @Override
        @Nonnull
        public TaggedShapelessCraftingRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            NonNullList<Ingredient> ingredients = readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));

            JsonObject resultJson = GsonHelper.getAsJsonObject(json, "result");
            if (resultJson.has("tag")) {
                return new TaggedShapelessCraftingRecipe(recipeId, group, RecipeIngredient.fromTag(GsonHelper.getAsString(resultJson, "tag")), ingredients);
            } else {
                return new TaggedShapelessCraftingRecipe(recipeId, group, RecipeIngredient.fromItem(GsonHelper.getAsString(resultJson, "item")), ingredients);
            }
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
            NonNullList<Ingredient> list = NonNullList.create();

            for(int i = 0; i < ingredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
                if (!ingredient.isEmpty()) {
                    list.add(ingredient);
                }
            }

            return list;
        }

        @Nullable
        @Override
        public TaggedShapelessCraftingRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);
            int size = buffer.readVarInt();

            NonNullList<Ingredient> list = NonNullList.withSize(size, Ingredient.EMPTY);

            for (int idx = 0; idx < size; idx++) {
                list.set(idx, Ingredient.fromNetwork(buffer));
            }

            RecipeIngredient result = RecipeIngredient.read(buffer);
            return new TaggedShapelessCraftingRecipe(recipeId, group, result, list);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull TaggedShapelessCraftingRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeVarInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            recipe.result.write(buffer);
        }
    }

    // endregion
}
