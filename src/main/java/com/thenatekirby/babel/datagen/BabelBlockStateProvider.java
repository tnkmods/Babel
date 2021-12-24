package com.thenatekirby.babel.datagen;

import com.thenatekirby.babel.core.api.IBlockProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class BabelBlockStateProvider extends BlockStateProvider {
    public static final String BLOCK_FOLDER = ModelProvider.BLOCK_FOLDER;
    private final String modId;

    public BabelBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
        this.modId = modid;
    }

    @Override
    protected void registerStatesAndModels() {
        this.registerStatesAndModels(new ModelBuilder());
    }

    protected void registerStatesAndModels(@Nonnull ModelBuilder builder) {
    }

    @Nonnull
    @Override
    public String getName() {
        return modId + ":" + "block_states";
    }

    // ====---------------------------------------------------------------------------====

    public class ModelBuilder {
        // ====---------------------------------------------------------------------------====
        // region Cube All

        public void cubeAll(IBlockProvider blockProvider) {
            String name = blockProvider.asBlock().getRegistryName().getPath();
            this.cubeAll(blockProvider, name, name);
        }

        public void cubeAll(IBlockProvider blockProvider, String texture) {
            String name = blockProvider.asBlock().getRegistryName().getPath();
            this.cubeAll(blockProvider, name, texture);
        }

        public void cubeAll(IBlockProvider blockProvider, String name, String texture) {
            ModelFile modelFile = models().cubeAll(BLOCK_FOLDER + "/" + name, modLoc("block/" + texture));
            simpleBlock(blockProvider.asBlock(), modelFile);
        }

        // endregion
        // ====---------------------------------------------------------------------------====
        // region Orientable With Bottom

        public void orientableWithBottom(IBlockProvider blockProvider, String topTexture, String bottomTexture, String sideTexture, String frontTexture) {
            this.orientableWithBottom(blockProvider, blockProvider.asBlock().getRegistryName().getPath(), topTexture, bottomTexture, sideTexture, frontTexture);
        }

        public void orientableWithBottom(IBlockProvider blockProvider, String name, String topTexture, String bottomTexture, String sideTexture, String frontTexture) {
            ResourceLocation topLoc = modLoc("block/" + topTexture);
            ResourceLocation bottomLoc = modLoc("block/" + bottomTexture);
            ResourceLocation sideLoc = modLoc("block/" + sideTexture);
            ResourceLocation frontLoc = modLoc("block/" + frontTexture);

            BlockModelBuilder blockModelBuilder = models().orientableWithBottom(name, sideLoc, frontLoc, bottomLoc, topLoc);
            horizontalBlock(blockProvider.asBlock(), blockModelBuilder);
        }

        // endregion
        // ====---------------------------------------------------------------------------====
        // region Slabs

        public void slab(IBlockProvider blockProvider, String texture) {
            this.slab(blockProvider, blockProvider.asBlock().getRegistryName().getPath(), texture);
        }

        public void slab(IBlockProvider blockProvider, String name, String texture) {
            ResourceLocation textureLoc = modLoc("block/" + texture);
            ModelFile bottom = models().slab(name, textureLoc, textureLoc, textureLoc);
            ModelFile top = models().slabTop(name + "_top", textureLoc, textureLoc, textureLoc);
            ModelFile doubleslab = models().getExistingFile(modLoc("block/" + texture));

            getVariantBuilder(blockProvider.asBlock())
                    .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(new ConfiguredModel(bottom))
                    .partialState().with(SlabBlock.TYPE, SlabType.TOP).addModels(new ConfiguredModel(top))
                    .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(new ConfiguredModel(doubleslab));
        }

        // endregion
        // ====---------------------------------------------------------------------------====
        // region Stairs

        public void stairs(IBlockProvider blockProvider, String texture) {
            this.stairs(blockProvider, blockProvider.asBlock().getRegistryName().getPath(), texture);
        }

        public void stairs(IBlockProvider blockProvider, String name, String texture) {
            ResourceLocation textureLoc = modLoc("block/" + texture);
            ModelFile stairs = models().stairs(name, textureLoc, textureLoc, textureLoc);
            ModelFile stairsInner = models().stairsInner(name + "_inner", textureLoc, textureLoc, textureLoc);
            ModelFile stairsOuter = models().stairsOuter(name + "_outer", textureLoc, textureLoc, textureLoc);
            stairsBlock((StairBlock) blockProvider.asBlock(), stairs, stairsInner, stairsOuter);
        }

        // endregion
        // ====---------------------------------------------------------------------------====
        // region Walls

        public void wall(IBlockProvider blockProvider, String texture) {
            this.wall(blockProvider, blockProvider.asBlock().getRegistryName().getPath(), texture);
        }

        public void wall(IBlockProvider blockProvider, String name, String texture) {
            ResourceLocation textureLoc = modLoc("block/" + texture);
            ModelFile wallPost = models().wallPost(name + "_post", textureLoc);
            ModelFile wallSide = models().wallSide(name + "_side", textureLoc);
            ModelFile wallSideTall = models().wallSideTall(name + "_side_tall", textureLoc);
            wallBlock((WallBlock) blockProvider.asBlock(), wallPost, wallSide, wallSideTall);

            models().withExistingParent(name + "_inventory", "minecraft:block/wall_inventory")
                    .texture("wall", textureLoc);
        }

        // endregion
    }
}
