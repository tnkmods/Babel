package com.thenatekirby.babel.recipe.manager;

import com.thenatekirby.babel.core.api.IRecipeCollection;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class RecipeCollectionManager {
    private static final RecipeCollectionManager INSTANCE = new RecipeCollectionManager();
    private RecipeManager recipeManager;
    private final List<IRecipeCollection<?>> collections = new ArrayList<>();

    // ====---------------------------------------------------------------------------====
    // region Collections

    public static void addRecipeCollection(@Nonnull IRecipeCollection<?> recipeCollection) {
        INSTANCE.collections.add(recipeCollection);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Recipe Management

    public static void setRecipeManager(@Nonnull RecipeManager manager) {
        INSTANCE.recipeManager = manager;
    }

    public static void onReloadRecipes() {
        RecipeManager recipeManager = INSTANCE.recipeManager;
        if (recipeManager == null) {
            return;
        }

        onReloadRecipes(recipeManager);
    }

    public static void onReloadRecipes(RecipeManager recipeManager) {
        INSTANCE.collections.forEach(collection -> collection.reloadRecipes(recipeManager));
    }

    // endregion
}
