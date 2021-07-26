package com.thenatekirby.babel.datagen;

import com.thenatekirby.babel.api.IBlockProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.util.Objects;

public class BabelItemModelProvider extends ItemModelProvider {
    private final String modId;
    public BabelItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
        this.modId = modid;
    }

    @Override
    protected void registerModels() {
        this.registerModels(new ModelBuilder());
    }

    protected void registerModels(@Nonnull ModelBuilder builder) {
    }

    @Nonnull
    @Override
    public String getName() {
        return modId + ":item_models";
    }

    // ====---------------------------------------------------------------------------====
    // region Builder

    public class ModelBuilder {
        public void item(IItemProvider itemProvider) {
            String name = Objects.requireNonNull(itemProvider.asItem().getRegistryName()).getPath();
            ResourceLocation modelFile = modLoc("item/" + name);
            getBuilder(name).parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", modelFile);
        }

        public void blockItem(IBlockProvider blockProvider) {
            String name = Objects.requireNonNull(blockProvider.asBlock().getRegistryName()).getPath();
            ResourceLocation modelFile = modLoc("block/" + name);
            getBuilder(name)
                    .parent(new ModelFile.UncheckedModelFile(modelFile));
        }

        public void wallItem(IBlockProvider blockProvider) {
            String name = Objects.requireNonNull(blockProvider.asBlock().getRegistryName()).getPath();
            ResourceLocation modelFile = modLoc("block/" + name + "_inventory");
            getBuilder(name)
                    .parent(new ModelFile.UncheckedModelFile(modelFile));
        }
    }

    // endregion
}
