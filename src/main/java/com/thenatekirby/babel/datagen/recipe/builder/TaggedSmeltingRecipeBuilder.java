package com.thenatekirby.babel.datagen.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thenatekirby.babel.babelmod.BabelSerializers;
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

/**
 * A builder for data generating TaggedSmeltingRecipes.
 */

@SuppressWarnings("unused")
public class TaggedSmeltingRecipeBuilder {
    private final ResourceLocation recipeId;

    private RecipeIngredient input;
    private RecipeIngredient output;
    private float experience = 0.1F;
    private int cookingTime = 200;
    private List<JsonObject> conditions = new ArrayList<>();

    private TaggedSmeltingRecipeBuilder(@Nonnull ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }

    public static TaggedSmeltingRecipeBuilder builder(@Nonnull ResourceLocation recipeId) {
        return new TaggedSmeltingRecipeBuilder(recipeId);
    }

    // ====---------------------------------------------------------------------------====
    // region Properties

    public TaggedSmeltingRecipeBuilder withInput(RecipeIngredient input) {
        this.input = input;
        return this;
    }

    public TaggedSmeltingRecipeBuilder withOutput(RecipeIngredient output) {
        this.output = output;
        return this;
    }

    public TaggedSmeltingRecipeBuilder withCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public TaggedSmeltingRecipeBuilder withExperience(float experience) {
        this.experience = experience;
        return this;
    }

    public TaggedSmeltingRecipeBuilder withCondition(IRecipeCondition condition) {
        this.conditions.add(condition.serializeJson());
        return this;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Builder

    public void build(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new Result(this.recipeId, this.input, this.output, this.experience, this.cookingTime, this.conditions));
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region IFinishedRecipe

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final RecipeIngredient input;
        private final RecipeIngredient output;
        private final float experience;
        private final int cookingTime;
        private final List<JsonObject> conditions;

        public Result(ResourceLocation recipeId, RecipeIngredient input, RecipeIngredient output, float experience, int cookingTime, List<JsonObject> conditions) {
            this.id = recipeId;
            this.input = input;
            this.output = output;
            this.experience = experience;
            this.cookingTime = cookingTime;
            this.conditions = conditions;
        }

        public void serializeRecipeData(@Nonnull JsonObject json) {
            if (!conditions.isEmpty()) {
                JsonArray conditions = new JsonArray();
                for (JsonObject condition : this.conditions) {
                    conditions.add(condition);
                }

                json.add("conditions", conditions);
            }

            json.add("ingredient", input.serializeJson());
            json.add("result", output.serializeJson());
            json.addProperty("experience", this.experience);
            json.addProperty("cookingtime", this.cookingTime);
        }

        @Nonnull
        public RecipeSerializer<?> getType() {
            return BabelSerializers.TAGGED_SMELTING.getAsRecipeSerializer();
        }

        @Nonnull
        public ResourceLocation getId() {
            return this.id;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }

    // endregion
}
