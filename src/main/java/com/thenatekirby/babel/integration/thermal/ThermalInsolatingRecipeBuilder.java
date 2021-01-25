package com.thenatekirby.babel.integration.thermal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thenatekirby.babel.condition.IRecipeCondition;
import com.thenatekirby.babel.condition.ModLoadedCondition;
import com.thenatekirby.babel.core.RecipeIngredient;
import com.thenatekirby.babel.core.RecipeOutput;
import com.thenatekirby.babel.integration.Mods;
import com.thenatekirby.babel.recipe.IExternalRecipe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ThermalInsolatingRecipeBuilder {
    private final ResourceLocation recipeId;

    private RecipeIngredient input;
    private List<RecipeOutput> outputs = new ArrayList<>();
    private float waterMod = 1.0f;
    private float energyMod = 1.0f;
    private List<JsonObject> conditions = new ArrayList<>();

    private ThermalInsolatingRecipeBuilder(@Nonnull ResourceLocation recipeId) {
        this.recipeId = recipeId;
        this.conditions.add(new ModLoadedCondition("thermal").serializeJson());
    }

    public static ThermalInsolatingRecipeBuilder builder(@Nonnull ResourceLocation recipeId) {
        return new ThermalInsolatingRecipeBuilder(recipeId);
    }

    public ThermalInsolatingRecipeBuilder withInput(@Nonnull RecipeIngredient input) {
        this.input = input;
        return this;
    }

    public ThermalInsolatingRecipeBuilder withOutput(@Nonnull RecipeOutput output) {
        this.outputs.add(output);
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

    public void build(Consumer<IExternalRecipe> consumer) {
        consumer.accept(new ThermalInsolatingRecipeBuilder.Result(this.recipeId, this.input, this.outputs, this.conditions, this.waterMod, this.energyMod));
    }

    public static class Result implements IExternalRecipe {
        private final ResourceLocation recipeId;
        private final RecipeIngredient input;
        private final List<RecipeOutput> outputs;
        private final List<JsonObject> conditions;
        private final float waterMod;
        private final float energyMod;

        Result(@Nonnull ResourceLocation recipeId, @Nonnull RecipeIngredient input, @Nonnull List<RecipeOutput> outputs, @Nonnull List<JsonObject> conditions, float waterMod, float energyMod) {
            this.recipeId = recipeId;
            this.input = input;
            this.outputs = outputs;
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

            json.add("ingredient", this.input.serializeJson());

            JsonArray resultsArray = new JsonArray();
            for (RecipeOutput output : outputs) {
                JsonObject resultJson = new JsonObject();
                resultJson.addProperty("item", output.getResultId());
                resultJson.addProperty("chance", output.getChance());
                resultsArray.add(resultJson);
            }

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
