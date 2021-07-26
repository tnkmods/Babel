package com.thenatekirby.babel.recipe.collection;

import com.thenatekirby.babel.core.inventory.InventoryContents;
import com.thenatekirby.babel.recipe.BabelRecipe;
import net.minecraft.item.crafting.RecipeManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public interface IRecipeCollection<T extends BabelRecipe> {
    Collection<T> getAllRecipes();

    @Nullable
    T firstRecipeMatching(@Nonnull InventoryContents contents);

    default boolean hasRecipeMatching(@Nonnull InventoryContents contents) {
        return firstRecipeMatching(contents) != null;
    }

    void reloadRecipes(@Nonnull RecipeManager recipeManager);
}
