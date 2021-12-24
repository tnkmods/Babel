package com.thenatekirby.babel.core.api;

import com.thenatekirby.babel.machine.inventory.MachineContents;
import com.thenatekirby.babel.recipe.BabelRecipe;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

// ====---------------------------------------------------------------------------====

public interface IRecipeCollection<T extends BabelRecipe> {
    Collection<T> getAllRecipes();

    @Nullable
    T firstRecipeMatching(@Nonnull MachineContents contents);

    default boolean hasRecipeMatching(@Nonnull MachineContents contents) {
        return firstRecipeMatching(contents) != null;
    }

    void reloadRecipes(@Nonnull RecipeManager recipeManager);
}
