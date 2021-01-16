package com.thenatekirby.babel.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thenatekirby.babel.condition.IRecipeCondition;
import com.thenatekirby.babel.mod.BabelSerializers;
import com.thenatekirby.babel.recipe.component.RecipeIngredient;
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
 * A builder for data generating TaggedShapelessRecipes.
 */

@SuppressWarnings("unused")
public class TaggedShapelessRecipeBuilder {
    private final ResourceLocation recipeId;

    private RecipeIngredient result;

    private List<RecipeIngredient> ingredients = new ArrayList<>();
    private List<JsonObject> conditions = new ArrayList<>();

    private TaggedShapelessRecipeBuilder(@Nonnull ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }

    public static TaggedShapelessRecipeBuilder builder(@Nonnull ResourceLocation recipeId) {
        return new TaggedShapelessRecipeBuilder(recipeId);
    }

    // ====---------------------------------------------------------------------------====
    // region Properties

    public TaggedShapelessRecipeBuilder withInput(RecipeIngredient ingredient) {
        ingredients.add(ingredient);
        return this;
    }

    public TaggedShapelessRecipeBuilder withInput(RecipeIngredient ingredient, int count) {
        for (int idx = 0; idx < count; idx++) {
            ingredients.add(ingredient);
        }

        return this;
    }

    public TaggedShapelessRecipeBuilder withOutput(RecipeIngredient result) {
        this.result = result;
        return this;
    }

    public TaggedShapelessRecipeBuilder withCondition(IRecipeCondition condition) {
        this.conditions.add(condition.serializeJson());
        return this;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Builder

    public void build(Consumer<IFinishedRecipe> consumer) {
        consumer.accept(new TaggedShapelessRecipeBuilder.Result(this.recipeId, this.ingredients, this.result, this.conditions));
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region IFinishedRecipe

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation recipeId;
        private final List<RecipeIngredient> ingredients;
        private final RecipeIngredient result;
        private final List<JsonObject> conditions;

        Result(@Nonnull ResourceLocation recipeId, @Nonnull List<RecipeIngredient> ingredients, RecipeIngredient result,  @Nonnull List<JsonObject> conditions) {
            this.recipeId = recipeId;
            this.ingredients = ingredients;
            this.result = result;
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

            JsonArray jsonarray = new JsonArray();

            for(RecipeIngredient ingredient : this.ingredients) {
                jsonarray.add(ingredient.serializeJson());
            }

            json.add("ingredients", jsonarray);
            json.add("result", result.serializeJson());
        }

        @Nonnull
        public IRecipeSerializer<?> getSerializer() {
            return BabelSerializers.TAGGED_SHAPELESS.getAsRecipeSerializer();
        }

        @Nonnull
        public ResourceLocation getID() {
            return this.recipeId;
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
