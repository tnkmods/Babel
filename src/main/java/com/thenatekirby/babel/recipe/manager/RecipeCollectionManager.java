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

    private List<IRecipeCollection> collections = new ArrayList<>();

    public static void setRecipeManager(@Nonnull RecipeManager manager) {
        INSTANCE.recipeManager = manager;
    }

    public static void addRecipeCollection(@Nonnull IRecipeCollection recipeCollection) {
        INSTANCE.collections.add(recipeCollection);
    }

    public static void onServerRecipeReload() {
        RecipeManager recipeManager = INSTANCE.recipeManager;
        if (recipeManager == null) {
            return;
        }

        INSTANCE.collections.forEach(collection -> collection.reloadRecipes(recipeManager));
    }

    public static void onClientRecipeReload(RecipeManager recipeManager) {
        INSTANCE.collections.forEach(collection -> collection.reloadRecipes(recipeManager));
    }
}
