package com.thenatekirby.babel.datagen.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thenatekirby.babel.core.api.IItemProvider;
import com.thenatekirby.babel.core.api.IRecipeCondition;
import com.thenatekirby.babel.recipe.components.RecipeIngredient;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// ====---------------------------------------------------------------------------====

public class StonecuttingRecipeBuilder {
    private final ResourceLocation recipeId;
    private RecipeIngredient ingredient;
    private IItemProvider result;
    private int count;
    private List<IRecipeCondition> conditions = new ArrayList<>();

    private StonecuttingRecipeBuilder(@Nonnull ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }

    public static StonecuttingRecipeBuilder builder(@Nonnull ResourceLocation recipeId) {
        return new StonecuttingRecipeBuilder(recipeId);
    }

    public StonecuttingRecipeBuilder withInput(@Nonnull RecipeIngredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public StonecuttingRecipeBuilder withResult(@Nonnull IItemProvider itemProvider, int count) {
        this.result = itemProvider;
        this.count = count;
        return this;
    }

    public StonecuttingRecipeBuilder withCondition(IRecipeCondition condition) {
        this.conditions.add(condition);
        return this;
    }

    public void build(Consumer<FinishedRecipe> consumerIn) {
        consumerIn.accept(new Result(recipeId, ingredient, result, count, conditions));
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region FinishedRecipe

    public static class Result implements FinishedRecipe {
        private final ResourceLocation recipeId;
        private final RecipeIngredient ingredient;
        private final IItemProvider result;
        private final int count;
        private final List<IRecipeCondition> conditions;

        Result(@Nonnull ResourceLocation recipeId, @Nonnull RecipeIngredient ingredient, @Nonnull IItemProvider result, int count, List<IRecipeCondition> conditions) {
            this.recipeId = recipeId;
            this.ingredient = ingredient;
            this.result = result;
            this.count = count;
            this.conditions = conditions;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (!this.conditions.isEmpty()) {
                JsonArray conditionsJson = new JsonArray();
                for (IRecipeCondition condition : conditions) {
                    conditionsJson.add(condition.serializeJson());
                }

                json.add("conditions", conditionsJson);
            }

            json.add("ingredient", ingredient.serializeJson());
            json.addProperty("result", result.asItem().getRegistryName().toString());
            json.addProperty("count", count);
        }

        @Override
        @Nonnull
        public ResourceLocation getId() {
            return recipeId;
        }

        @Override
        @Nonnull
        public RecipeSerializer<?> getType() {
            return RecipeSerializer.STONECUTTER;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
    // endregion
}
