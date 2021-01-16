package com.thenatekirby.babel.datagen;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.recipe.IExternalRecipe;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class ExternalRecipeProvider implements IDataProvider {
    private final DataGenerator dataGenerator;
    private final String modId;

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    public ExternalRecipeProvider(@Nonnull String modId, @Nonnull DataGenerator dataGenerator) {
        this.modId = modId;
        this.dataGenerator = dataGenerator;
    }

    @Override
    public void act(@Nonnull DirectoryCache cache) throws IOException {
        Path path = this.dataGenerator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();

        registerRecipes(recipe -> {
            if (!set.add(recipe.getId())) {
                throw new IllegalStateException("Duplicate recipe " + recipe.getId());

            } else {
                saveRecipe(cache, recipe.getRecipeJson(), path.resolve("data/" + recipe.getId().getNamespace() + "/recipes/" + recipe.getId().getPath() + ".json"));
            }
        });
    }

    public void registerRecipes(Consumer<IExternalRecipe> consumer) {
    }

    private static void saveRecipe(DirectoryCache cache, JsonObject recipeJson, Path outputPath) {
        try {
            String json = GSON.toJson(recipeJson);
            String hash = HASH_FUNCTION.hashUnencodedChars(json).toString();
            if (!Objects.equals(cache.getPreviousHash(outputPath), hash) || !Files.exists(outputPath)) {
                Files.createDirectories(outputPath.getParent());

                try (BufferedWriter bufferedwriter = Files.newBufferedWriter(outputPath)) {
                    bufferedwriter.write(json);
                }
            }

            cache.recordHash(outputPath, hash);

        } catch (IOException ioexception) {
            Babel.getLogger().error("babel: Couldn't save recipe {}", recipeJson, ioexception);
        }
    }

    @Override
    @Nonnull
    public String getName() {
        return modId + ":external_recipes";
    }
}
