package com.thenatekirby.babel.datagen.recipe;

import com.thenatekirby.babel.core.api.IRecipeRegistrator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.Consumer;

// ====---------------------------------------------------------------------------====

public class BabelRecipeProvider extends RecipeProvider {
    public BabelRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    protected void addRecipeRegistrators(@Nonnull Consumer<FinishedRecipe> consumer, IRecipeRegistrator... registrators) {
        Arrays.stream(registrators).forEach(registrator -> {
            registrator.registerRecipes(consumer);
        });
    }
}
