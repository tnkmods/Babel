package com.thenatekirby.babel.babelmod;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.recipe.manager.RecipeCollectionManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

// ====---------------------------------------------------------------------------====

@Mod.EventBusSubscriber(modid = Babel.MOD_ID)
public class BabelEventHandler {
    @SubscribeEvent
    public static void onAddReloadListener(final AddReloadListenerEvent event) {
        Babel.getLogger().info("ON ADD RELOAD LISTENER EVENT");
        // TODO: Remap
//        event.addListener(new PreparableReloadListener() {
//            @Override
//            protected Void prepare(@Nonnull ResourceManager resourceManagerIn, @Nonnull IProfiler profilerIn) {
//                RecipeCollectionManager.setRecipeManager(event.getDataPackRegistries().getRecipeManager());
//                return null;
//            }
//
//            @Override
//            protected void apply(@Nonnull Void objectIn, @Nonnull IResourceManager resourceManagerIn, @Nonnull IProfiler profilerIn) {
//            }
//        });
    }

    @SubscribeEvent
    public static void onTagsUpdated(final TagsUpdatedEvent event) {
        RecipeCollectionManager.onServerRecipeReload();
    }

    // TODO: Remap
//    @SubscribeEvent
//    public static void onModIdMapping(final FMLModIdMappingEvent event) {
//        RecipeCollectionManager.onServerRecipeReload();
//    }

    @SubscribeEvent
    public static void onRecipesUpdated(final RecipesUpdatedEvent event) {
        RecipeCollectionManager.onClientRecipeReload(event.getRecipeManager());
    }
}
