package com.thenatekirby.babel.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thenatekirby.babel.mod.BabelSerializers;
import com.thenatekirby.babel.core.RecipeIngredient;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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
    public ItemStack assemble(CraftingInventory inv) {
        return getResultItem().copy();
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return BabelSerializers.TAGGED_SHAPELESS.getAsRecipeSerializer();
    }

    // ====---------------------------------------------------------------------------====
    // region Serializer

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<TaggedShapelessCraftingRecipe> {
        @Override
        @Nonnull
        public TaggedShapelessCraftingRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            String group = JSONUtils.getAsString(json, "group", "");
            NonNullList<Ingredient> ingredients = readIngredients(JSONUtils.getAsJsonArray(json, "ingredients"));

            JsonObject resultJson = JSONUtils.getAsJsonObject(json, "result");
            if (resultJson.has("tag")) {
                return new TaggedShapelessCraftingRecipe(recipeId, group, RecipeIngredient.fromTag(JSONUtils.getAsString(resultJson, "tag")), ingredients);
            } else {
                return new TaggedShapelessCraftingRecipe(recipeId, group, RecipeIngredient.fromItem(JSONUtils.getAsString(resultJson, "item")), ingredients);
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
        public TaggedShapelessCraftingRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
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
        public void toNetwork(@Nonnull PacketBuffer buffer, @Nonnull TaggedShapelessCraftingRecipe recipe) {
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
