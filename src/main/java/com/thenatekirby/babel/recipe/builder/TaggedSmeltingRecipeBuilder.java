package com.thenatekirby.babel.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thenatekirby.babel.condition.IRecipeCondition;
import com.thenatekirby.babel.mod.BabelSerializers;
import com.thenatekirby.babel.core.RecipeIngredient;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;

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

    public void build(Consumer<IFinishedRecipe> consumer) {
        consumer.accept(new Result(this.recipeId, this.input, this.output, this.experience, this.cookingTime, this.conditions));
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region IFinishedRecipe

    public static class Result implements IFinishedRecipe {
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

        public void serialize(@Nonnull JsonObject json) {
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
        public IRecipeSerializer<?> getSerializer() {
            return BabelSerializers.TAGGED_SMELTING.getAsRecipeSerializer();
        }

        @Nonnull
        public ResourceLocation getID() {
            return this.id;
        }

        @Nullable
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }

    // endregion
}
