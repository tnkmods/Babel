package com.thenatekirby.babel.datagen;

import com.thenatekirby.babel.api.IRecipeRegistrator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.Consumer;

public class BabelRecipeProvider extends RecipeProvider {
    public BabelRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    protected void addRecipeRegistrators(@Nonnull Consumer<IFinishedRecipe> consumer, IRecipeRegistrator... registrators) {
        Arrays.stream(registrators).forEach(registrator -> {
            registrator.registerRecipes(consumer);
        });
    }
}
