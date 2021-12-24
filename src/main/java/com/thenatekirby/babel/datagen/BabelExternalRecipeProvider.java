package com.thenatekirby.babel.datagen;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.core.api.IExternalRecipe;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

// ====---------------------------------------------------------------------------====

public class BabelExternalRecipeProvider implements DataProvider {
    private final DataGenerator dataGenerator;
    private final String modId;

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    public BabelExternalRecipeProvider(@Nonnull String modId, @Nonnull DataGenerator dataGenerator) {
        this.modId = modId;
        this.dataGenerator = dataGenerator;
    }

    @Override
    public void run(@Nonnull HashCache cache) throws IOException {
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

    private static void saveRecipe(HashCache cache, JsonObject recipeJson, Path outputPath) {
        try {
            String json = GSON.toJson(recipeJson);
            String hash = SHA1.hashUnencodedChars(json).toString();
            if (!Objects.equals(cache.getHash(outputPath), hash) || !Files.exists(outputPath)) {
                Files.createDirectories(outputPath.getParent());

                try (BufferedWriter bufferedwriter = Files.newBufferedWriter(outputPath)) {
                    bufferedwriter.write(json);
                }
            }

            cache.putNew(outputPath, hash);

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
