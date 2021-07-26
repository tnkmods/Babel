package com.thenatekirby.babel.api;

import net.minecraft.data.IFinishedRecipe;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public interface IRecipeRegistrator {
    void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer);
}
