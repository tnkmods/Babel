package com.thenatekirby.babel.recipe.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

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
public class ShapedRecipeBuilder  {
    private final ResourceLocation recipeId;
    private String result;
    private int count;
    private final List<String> pattern = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();

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

    public ShapedRecipeBuilder withKey(Character key, Ingredient ingredient) {
        this.key.put(key, ingredient);
        return this;
    }

    public ShapedRecipeBuilder withKey(Character key, IItemProvider itemProvider) {
        this.key.put(key, Ingredient.fromItems(itemProvider));
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

    public ShapedRecipeBuilder withResult(IItemProvider itemProvider) {
        return withResult(itemProvider, 1);
    }

    public ShapedRecipeBuilder withResult(IItemProvider itemProvider, int count) {
        return withResult(Objects.requireNonNull(itemProvider.asItem().getRegistryName()).toString(), count);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Builder

    public void build(Consumer<IFinishedRecipe> consumerIn) {
        consumerIn.accept(new Result(recipeId, pattern, key, result, count));
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region IFinishedRecipe

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation recipeId;
        private final List<String> pattern;
        private final Map<Character, Ingredient> key;
        private final String result;
        private final int count;

        public Result(ResourceLocation recipeId, List<String> pattern, Map<Character, Ingredient> key, String result, int count) {
            this.recipeId = recipeId;
            this.pattern = pattern;
            this.key = key;
            this.result = result;
            this.count = count;
        }

        public void serialize(@Nonnull JsonObject json) {
            JsonArray patternJson = new JsonArray();

            for (String line: this.pattern) {
                patternJson.add(line);
            }
            json.add("pattern", patternJson);

            JsonObject keyJson = new JsonObject();

            for(Map.Entry<Character, Ingredient> entry : this.key.entrySet()) {
                keyJson.add(String.valueOf(entry.getKey()), entry.getValue().serialize());
            }

            json.add("key", keyJson);

            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", this.result);

            if (this.count > 1) {
                resultJson.addProperty("count", this.count);
            }

            json.add("result", resultJson);
        }

        @Nonnull
        public IRecipeSerializer<?> getSerializer() {
            return IRecipeSerializer.CRAFTING_SHAPED;
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
