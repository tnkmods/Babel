package com.thenatekirby.babel.integration.botanypots;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thenatekirby.babel.condition.IRecipeCondition;
import com.thenatekirby.babel.condition.ModLoadedCondition;
import com.thenatekirby.babel.integration.Mods;
import com.thenatekirby.babel.recipe.IExternalRecipe;
import com.thenatekirby.babel.recipe.component.RecipeIngredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BotanyPotsSoilRecipeBuilder {
    private final ResourceLocation recipeId;

    private RecipeIngredient input;
    private String displayBlock;
    private List<String> categories = new ArrayList<>();
    private float growthModifier;

    private List<JsonObject> conditions = new ArrayList<>();

    private BotanyPotsSoilRecipeBuilder(@Nonnull ResourceLocation recipeId) {
        this.recipeId = recipeId;
        this.conditions.add(new ModLoadedCondition("botanypots").serializeJson());
    }

    public static BotanyPotsSoilRecipeBuilder builder(@Nonnull ResourceLocation recipeId) {
        return new BotanyPotsSoilRecipeBuilder(recipeId);
    }

    public BotanyPotsSoilRecipeBuilder withInput(@Nonnull RecipeIngredient input) {
        this.input = input;
        return this;
    }

    public BotanyPotsSoilRecipeBuilder withDisplayBlock(@Nonnull String displayBlock) {
        this.displayBlock = displayBlock;
        return this;
    }

    public BotanyPotsSoilRecipeBuilder withCategory(@Nonnull String category) {
        this.categories.add(category);
        return this;
    }

    public BotanyPotsSoilRecipeBuilder withGrowthModifier(float growthModifier) {
        this.growthModifier = growthModifier;
        return this;
    }

    public BotanyPotsSoilRecipeBuilder withCondition(@Nonnull IRecipeCondition condition) {
        this.conditions.add(condition.serializeJson());
        return this;
    }

    public void build(Consumer<IExternalRecipe> consumer) {
        consumer.accept(new BotanyPotsSoilRecipeBuilder.Result(this.recipeId, this.input, this.displayBlock, this.categories, this.growthModifier, this.conditions));
    }

    public static class Result implements IExternalRecipe {
        private final ResourceLocation recipeId;
        private RecipeIngredient input;
        private String displayBlock;
        private List<String> categories;
        private float growthModifier;
        private List<JsonObject> conditions;

        Result(@Nonnull ResourceLocation recipeId, @Nonnull RecipeIngredient input, @Nonnull String displayBlock, @Nonnull List<String> categories, float growthModifier, @Nonnull List<JsonObject> conditions) {
            this.recipeId = recipeId;
            this.input = input;
            this.displayBlock = displayBlock;
            this.categories = categories;
            this.growthModifier = growthModifier;
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

            json.add("input", input.serializeJson());

            JsonObject displayJson = new JsonObject();
            displayJson.addProperty("block", displayBlock);
            JsonObject propsJson = new JsonObject();
            propsJson.addProperty("moisture", 7);
            displayJson.add("properties", propsJson);
            json.add("display", displayJson);

            JsonArray categoriesJson = new JsonArray();
            for (String category : categories) {
                categoriesJson.add(category);
            }
            json.add("categories", categoriesJson);

            json.addProperty("growthModifier", growthModifier);
        }

        @Nonnull
        public ResourceLocation getId() {
            return this.recipeId;
        }

        @Nonnull
        @Override
        public ResourceLocation getType() {
            return Mods.BOTANY_POTS.withPath("soil");
        }
    }
}