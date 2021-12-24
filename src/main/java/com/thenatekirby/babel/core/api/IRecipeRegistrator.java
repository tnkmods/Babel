package com.thenatekirby.babel.core.api;

import net.minecraft.data.recipes.FinishedRecipe;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

// ====---------------------------------------------------------------------------====

public interface IRecipeRegistrator {
    void registerRecipes(@Nonnull Consumer<FinishedRecipe> consumer);
}
