package com.thenatekirby.babel.mod;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.recipe.manager.RecipeCollectionManager;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLModIdMappingEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Babel.MOD_ID)
public class BabelEventHandler {
    @SubscribeEvent
    public static void onAddReloadListener(final AddReloadListenerEvent event) {
        event.addListener(new ReloadListener<Void>() {
            @Override
            protected Void prepare(@Nonnull IResourceManager resourceManagerIn, @Nonnull IProfiler profilerIn) {
                RecipeCollectionManager.setRecipeManager(event.getDataPackRegistries().getRecipeManager());
                return null;
            }

            @Override
            protected void apply(@Nonnull Void objectIn, @Nonnull IResourceManager resourceManagerIn, @Nonnull IProfiler profilerIn) {
            }
        });
    }

    @SubscribeEvent
    public static void onTagsUpdated(final TagsUpdatedEvent.VanillaTagTypes event) {
        RecipeCollectionManager.onServerRecipeReload();
    }

    @SubscribeEvent
    public static void onModIdMapping(final FMLModIdMappingEvent event) {
        RecipeCollectionManager.onServerRecipeReload();
    }

    @SubscribeEvent
    public static void onRecipesUpdated(final RecipesUpdatedEvent event) {
        RecipeCollectionManager.onClientRecipeReload(event.getRecipeManager());
    }
}
