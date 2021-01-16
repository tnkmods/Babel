package com.thenatekirby.babel.integration.thermal;

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

public class ThermalInsolatingRecipeBuilder {
    private final ResourceLocation recipeId;

    private String input;
    private float waterMod = 1.0f;
    private float energyMod = 1.0f;
    private String output;
    private float chance = 1.0f;

    private List<JsonObject> conditions = new ArrayList<>();

    private ThermalInsolatingRecipeBuilder(@Nonnull ResourceLocation recipeId) {
        this.recipeId = recipeId;
        this.conditions.add(new ModLoadedCondition("thermal").serializeJson());
    }

    public static ThermalInsolatingRecipeBuilder builder(@Nonnull ResourceLocation recipeId) {
        return new ThermalInsolatingRecipeBuilder(recipeId);
    }

    public ThermalInsolatingRecipeBuilder withInput(@Nonnull String input) {
        this.input = input;
        return this;
    }

    public ThermalInsolatingRecipeBuilder withOutput(@Nonnull String id) {
        this.output = id;
        return this;
    }

    public ThermalInsolatingRecipeBuilder withCondition(@Nonnull IRecipeCondition condition) {
        this.conditions.add(condition.serializeJson());
        return this;
    }

    public ThermalInsolatingRecipeBuilder withWaterMod(float waterMod) {
        this.waterMod = waterMod;
        return this;
    }

    public ThermalInsolatingRecipeBuilder withEnergyMod(float energyMod) {
        this.energyMod = energyMod;
        return this;
    }

    public ThermalInsolatingRecipeBuilder withChance(float chance) {
        this.chance = chance;
        return this;
    }

    public void build(Consumer<IExternalRecipe> consumer) {
        consumer.accept(new ThermalInsolatingRecipeBuilder.Result(this.recipeId, this.input, this.output, this.chance, this.conditions, this.waterMod, this.energyMod));
    }

    public static class Result implements IExternalRecipe {
        private final ResourceLocation recipeId;
        private final String input;
        private final String output;
        private final float chance;
        private final List<JsonObject> conditions;
        private final float waterMod;
        private final float energyMod;

        Result(@Nonnull ResourceLocation recipeId, @Nonnull String input, @Nonnull String output, float chance, @Nonnull List<JsonObject> conditions, float waterMod, float energyMod) {
            this.recipeId = recipeId;
            this.input = input;
            this.output = output;
            this.chance = chance;
            this.conditions = conditions;
            this.waterMod = waterMod;
            this.energyMod = energyMod;
        }

        public void serialize(@Nonnull JsonObject json) {
            if (!conditions.isEmpty()) {
                JsonArray conditions = new JsonArray();
                for (JsonObject condition : this.conditions) {
                    conditions.add(condition);
                }

                json.add("conditions", conditions);
            }

            JsonObject ingredientJson = new JsonObject();
            ingredientJson.addProperty("item", this.input);
            json.add("ingredient", ingredientJson);

            JsonArray resultsArray = new JsonArray();
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", output);
            resultJson.addProperty("chance", chance);
            resultsArray.add(resultJson);

            json.add("result", resultsArray);

            json.addProperty("waterMod", waterMod);
            json.addProperty("energyMod", energyMod);
        }

        @Nonnull
        public ResourceLocation getId() {
            return this.recipeId;
        }

        @Nonnull
        @Override
        public ResourceLocation getType() {
            return Mods.THERMAL.withPath("insolator");
        }
    }
}
