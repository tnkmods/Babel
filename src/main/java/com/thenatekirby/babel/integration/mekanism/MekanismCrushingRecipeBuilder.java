package com.thenatekirby.babel.integration.mekanism;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thenatekirby.babel.condition.IRecipeCondition;
import com.thenatekirby.babel.condition.ModLoadedCondition;
import com.thenatekirby.babel.integration.Mods;
import com.thenatekirby.babel.recipe.IExternalRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MekanismCrushingRecipeBuilder {
    private final ResourceLocation recipeId;

    private String input;
    private String output;
    private int count;

    private List<JsonObject> conditions = new ArrayList<>();

    private MekanismCrushingRecipeBuilder(@Nonnull ResourceLocation recipeId) {
        this.recipeId = recipeId;
        this.conditions.add(new ModLoadedCondition("mekanism").serializeJson());
    }

    public static MekanismCrushingRecipeBuilder builder(@Nonnull ResourceLocation recipeId) {
        return new MekanismCrushingRecipeBuilder(recipeId);
    }

    public MekanismCrushingRecipeBuilder withInput(@Nonnull String input) {
        this.input = input;
        return this;
    }

    public MekanismCrushingRecipeBuilder withOutput(@Nonnull ItemStack output) {
        this.output = output.getItem().getRegistryName().toString();
        this.count = output.getCount();
        return this;
    }

    public MekanismCrushingRecipeBuilder withOutput(@Nonnull String id, int count) {
        this.output = id;
        this.count = count;
        return this;
    }

    public MekanismCrushingRecipeBuilder withCondition(@Nonnull IRecipeCondition condition) {
        this.conditions.add(condition.serializeJson());
        return this;
    }

    public void build(Consumer<IExternalRecipe> consumer) {
        consumer.accept(new MekanismCrushingRecipeBuilder.Result(this.recipeId, this.input, this.output, this.count, this.conditions));
    }

    public static class Result implements IExternalRecipe {
        private final ResourceLocation recipeId;
        private final String output;
        private final int count;
        private final String input;
        private final List<JsonObject> conditions;

        Result(@Nonnull ResourceLocation recipeId, @Nonnull String input, @Nonnull String output, int count, @Nonnull List<JsonObject> conditions) {
            this.recipeId = recipeId;
            this.input = input;
            this.output = output;
            this.count = count;
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

            JsonObject inputJson = new JsonObject();
            JsonObject ingredientJson = new JsonObject();
            ingredientJson.addProperty("item", this.input);
            inputJson.add("ingredient", ingredientJson);
            json.add("input", inputJson);

            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", output);
            if (count > 1) {
                resultJson.addProperty("count", count);
            }

            json.add("output", resultJson);
        }

        @Nonnull
        public ResourceLocation getId() {
            return this.recipeId;
        }

        @Nonnull
        @Override
        public ResourceLocation getType() {
            return Mods.MEKANISM.withPath("crushing");
        }
    }
}