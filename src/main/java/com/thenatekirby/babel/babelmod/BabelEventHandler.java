package com.thenatekirby.babel.babelmod;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.recipe.manager.RecipeCollectionManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

@Mod.EventBusSubscriber(modid = Babel.MOD_ID)
public class BabelEventHandler {
    @SubscribeEvent
    public static void onAddReloadListener(final AddReloadListenerEvent event) {
        event.addListener(new SimplePreparableReloadListener<RecipeManager>() {
            @Nonnull
            @Override
            protected RecipeManager prepare(@Nonnull ResourceManager resourceManager, @Nonnull ProfilerFiller profiler) {
                return event.getDataPackRegistries().getRecipeManager();
            }

            @Override
            protected void apply(@Nonnull RecipeManager recipeManager, @Nonnull ResourceManager resourceManager, @Nonnull ProfilerFiller profiler) {
                RecipeCollectionManager.setRecipeManager(recipeManager);
            }
        });
    }

    @SubscribeEvent
    public static void onTagsUpdated(final TagsUpdatedEvent event) {
        RecipeCollectionManager.onReloadRecipes();
    }

    @SubscribeEvent
    public static void onRecipesUpdated(final RecipesUpdatedEvent event) {
        RecipeCollectionManager.onReloadRecipes(event.getRecipeManager());
    }
}
