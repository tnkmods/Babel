package com.thenatekirby.babel.integration.botanypots;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thenatekirby.babel.condition.IRecipeCondition;
import com.thenatekirby.babel.condition.ModLoadedCondition;
import com.thenatekirby.babel.integration.Mods;
import com.thenatekirby.babel.recipe.IExternalRecipe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BotanyPotsCropRecipeBuilder {
    private final ResourceLocation recipeId;

    private String seed;
    private List<String> categories = new ArrayList<>();
    private int growthTicks;
    private String displayBlock;
    private String output;
    private float chance;

    private List<JsonObject> conditions = new ArrayList<>();

    private BotanyPotsCropRecipeBuilder(@Nonnull ResourceLocation recipeId) {
        this.recipeId = recipeId;
        this.conditions.add(new ModLoadedCondition("botanypots").serializeJson());
    }

    public static BotanyPotsCropRecipeBuilder builder(@Nonnull ResourceLocation recipeId) {
        return new BotanyPotsCropRecipeBuilder(recipeId);
    }

    public BotanyPotsCropRecipeBuilder withSeed(@Nonnull String seed) {
        this.seed = seed;
        return this;
    }

    public BotanyPotsCropRecipeBuilder withCategory(@Nonnull String category) {
        this.categories.add(category);
        return this;
    }

    public BotanyPotsCropRecipeBuilder withGrowthTicks(int growthTicks) {
        this.growthTicks = growthTicks;
        return this;
    }

    public BotanyPotsCropRecipeBuilder withDisplayBlock(@Nonnull String displayBlock) {
        this.displayBlock = displayBlock;
        return this;
    }

    public BotanyPotsCropRecipeBuilder withOutput(@Nonnull String id) {
        this.output = id;
        return this;
    }

    public BotanyPotsCropRecipeBuilder withChance(float chance) {
        this.chance = chance;
        return this;
    }

    public BotanyPotsCropRecipeBuilder withCondition(@Nonnull IRecipeCondition condition) {
        this.conditions.add(condition.serializeJson());
        return this;
    }

    public void build(Consumer<IExternalRecipe> consumer) {
        consumer.accept(new BotanyPotsCropRecipeBuilder.Result(this.recipeId, this.seed, this.categories, this.growthTicks, this.displayBlock, this.output, this.chance, this.conditions));
    }

    public static class Result implements IExternalRecipe {
        private final ResourceLocation recipeId;
        private final String seedId;
        private final List<String> categories;
        private final int growthTicks;
        private final String displayBlock;
        private final String output;
        private final float chance;
        private final List<JsonObject> conditions;

        Result(@Nonnull ResourceLocation recipeId, @Nonnull String seedId, @Nonnull List<String> categories, int growthTicks, @Nonnull String displayBlock, @Nonnull String output, float chance, @Nonnull List<JsonObject> conditions) {
            this.recipeId = recipeId;
            this.seedId = seedId;
            this.categories = categories;
            this.growthTicks = growthTicks;
            this.displayBlock = displayBlock;
            this.output = output;
            this.chance = chance;
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

            JsonObject seedJson = new JsonObject();
            seedJson.addProperty("item", seedId);
            json.add("seed", seedJson);

            JsonArray categoriesJson = new JsonArray();
            for (String category : categories) {
                categoriesJson.add(category);
            }
            json.add("categories", categoriesJson);

            json.addProperty("growthTicks", growthTicks);

            JsonObject displayJson = new JsonObject();
            displayJson.addProperty("block", displayBlock);
            JsonObject propsJson = new JsonObject();
            propsJson.addProperty("age", 3);
            displayJson.add("properties", propsJson);
            json.add("display", displayJson);

            JsonArray resultsJson = new JsonArray();

            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("chance", this.chance);
            JsonObject outputJson = new JsonObject();
            outputJson.addProperty("item", output);
            resultJson.add("output", outputJson);
            resultJson.addProperty("minRolls", 1);
            resultJson.addProperty("maxRolls", 1);

            resultsJson.add(resultJson);
            json.add("results", resultsJson);
        }

        @Nonnull
        public ResourceLocation getId() {
            return this.recipeId;
        }

        @Nonnull
        @Override
        public ResourceLocation getType() {
            return Mods.BOTANY_POTS.withPath("crop");
        }
    }
}