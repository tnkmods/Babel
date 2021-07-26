package com.thenatekirby.babel.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thenatekirby.babel.condition.IRecipeCondition;
import com.thenatekirby.babel.core.RecipeIngredient;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

    public void build(Consumer<IFinishedRecipe> consumerIn) {
        consumerIn.accept(new Result(recipeId, ingredient, result, count, conditions));
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region IFinishedRecipe

    public static class Result implements IFinishedRecipe {
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
        public IRecipeSerializer<?> getType() {
            return IRecipeSerializer.STONECUTTER;
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
