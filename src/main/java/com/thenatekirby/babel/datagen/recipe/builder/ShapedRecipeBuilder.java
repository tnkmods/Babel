package com.thenatekirby.babel.datagen.recipe.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thenatekirby.babel.core.api.IItemProvider;
import com.thenatekirby.babel.core.api.IRecipeCondition;
import com.thenatekirby.babel.recipe.components.RecipeIngredient;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

// ====---------------------------------------------------------------------------====

/**
 * A re-implementation of the Vanilla ShapedRecipeBuilder that follows the naming style
 * of our other recipe builders, and bypasses the need for achievements-based recipe
 * unlocking. If you want to gate the recipe by the achievement system (as Vanilla does),
 * then use the Vanilla ShapedRecipeBuilder.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class ShapedRecipeBuilder {
    private final ResourceLocation recipeId;
    private String result;
    private int count;
    private final List<String> pattern = Lists.newArrayList();
    private final Map<Character, RecipeIngredient> key = Maps.newLinkedHashMap();
    private final List<IRecipeCondition> conditions = Lists.newArrayList();

    private ShapedRecipeBuilder(@Nonnull ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }

    public static ShapedRecipeBuilder builder(@Nonnull ResourceLocation recipeId) {
        return new ShapedRecipeBuilder(recipeId);
    }

    // ====---------------------------------------------------------------------------====
    // region Properties

    public ShapedRecipeBuilder withPatternLine(String patternLine) {
        this.pattern.add(patternLine);
        return this;
    }

    public ShapedRecipeBuilder withKey(Character key, RecipeIngredient ingredient) {
        this.key.put(key, ingredient);
        return this;
    }

    public ShapedRecipeBuilder withResult(String result, int count) {
        this.result = result;
        this.count = count;
        return this;
    }

    public ShapedRecipeBuilder withResult(String result) {
        return withResult(result, 1);
    }

    public ShapedRecipeBuilder withResult(ItemLike itemProvider) {
        return withResult(itemProvider, 1);
    }

    public ShapedRecipeBuilder withResult(ItemLike itemProvider, int count) {
        return withResult(Objects.requireNonNull(itemProvider.asItem().getRegistryName()).toString(), count);
    }

    public ShapedRecipeBuilder withCondition(IRecipeCondition condition) {
        this.conditions.add(condition);
        return this;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Helpers

    public ShapedRecipeBuilder withStandardStoneWallRecipe(RecipeIngredient ingredient, IItemProvider result) {
        return this.withPatternLine("XXX")
                .withPatternLine("XXX")
                .withKey('X', ingredient)
                .withResult(result, 6);
    }

    public ShapedRecipeBuilder withStandardSlabRecipe(RecipeIngredient ingredient, IItemProvider result) {
        return this.withPatternLine("XXX")
                .withKey('X', ingredient)
                .withResult(result, 6);
    }

    public ShapedRecipeBuilder withStandardStairsRecipe(RecipeIngredient ingredient, IItemProvider result) {
        return this.withPatternLine("X  ")
                .withPatternLine("XX ")
                .withPatternLine("XXX")
                .withKey('X', ingredient)
                .withResult(result, 4);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Builder

    public void build(Consumer<FinishedRecipe> consumerIn) {
        consumerIn.accept(new Result(recipeId, pattern, key, result, conditions, count));
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region FinishedRecipe

    public static class Result implements FinishedRecipe {
        private final ResourceLocation recipeId;
        private final List<String> pattern;
        private final Map<Character, RecipeIngredient> key;
        private final String result;
        private final List<IRecipeCondition> conditions;
        private final int count;

        public Result(ResourceLocation recipeId, List<String> pattern, Map<Character, RecipeIngredient> key, String result, List<IRecipeCondition> conditions, int count) {
            this.recipeId = recipeId;
            this.pattern = pattern;
            this.key = key;
            this.result = result;
            this.conditions = conditions;
            this.count = count;
        }

        public void serializeRecipeData(@Nonnull JsonObject json) {
            JsonArray patternJson = new JsonArray();

            for (String line: this.pattern) {
                patternJson.add(line);
            }
            json.add("pattern", patternJson);

            JsonObject keyJson = new JsonObject();

            for(Map.Entry<Character, RecipeIngredient> entry : this.key.entrySet()) {
                keyJson.add(String.valueOf(entry.getKey()), entry.getValue().serializeJson());
            }

            json.add("key", keyJson);

            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", this.result);

            if (this.count > 1) {
                resultJson.addProperty("count", this.count);
            }

            if (!this.conditions.isEmpty()) {
                JsonArray conditions = new JsonArray();
                for (IRecipeCondition condition : this.conditions) {
                    conditions.add(condition.serializeJson());
                }

                json.add("conditions", conditions);
            }
            
            json.add("result", resultJson);
        }

        @Nonnull
        public RecipeSerializer<?> getType() {
            return RecipeSerializer.SHAPED_RECIPE;
        }

        @Nonnull
        public ResourceLocation getId() {
            return this.recipeId;
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
