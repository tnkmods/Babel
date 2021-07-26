package com.thenatekirby.babel.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.api.IBlockProvider;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.CopyName;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.SetContents;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BabelBlockLootTableProvider extends LootTableProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
    private final DataGenerator dataGenerator;
    private final String modId;

    public BabelBlockLootTableProvider(DataGenerator dataGeneratorIn, String modId) {
        super(dataGeneratorIn);
        this.dataGenerator = dataGeneratorIn;
        this.modId = modId;
    }

    @Override
    @Nonnull
    public String getName() {
        return modId + ":block_loot_tables";
    }

    // ====---------------------------------------------------------------------------====
    // region Lifecycle

    protected void addBlockLootTables(@Nonnull Builder builder) {
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
    }

    @Override
    public void act(@Nonnull DirectoryCache cache) {
        addBlockLootTables(new Builder());

        Map<ResourceLocation, LootTable> tables = new HashMap<>();
        for (Map.Entry<Block, LootTable.Builder> entry : lootTables.entrySet()) {
            tables.put(entry.getKey().getLootTable(), entry.getValue().setParameterSet(LootParameterSets.BLOCK).build());
        }

        writeLootTables(cache, tables);
    }

    private void writeLootTables(@Nonnull DirectoryCache cache, @Nonnull Map<ResourceLocation, LootTable> tables) {
        Path outputFolder = dataGenerator.getOutputFolder();

        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");

            try {
                IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path);

            } catch (IOException e) {
                Babel.getLogger().error("Unable to write loot table - {}", path, e);
                throw new RuntimeException(e);
            }
        });
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Builder

    public class Builder {
        public void blockDroppingSelf(IBlockProvider blockProvider) {
            String name = Objects.requireNonNull(blockProvider.asBlock().getRegistryName()).getPath();
            LootPool.Builder builder = LootPool.builder()
                    .name(name)
                    .rolls(ConstantRange.of(1))
                    .addEntry(ItemLootEntry.builder(blockProvider.asBlock()));

            LootTable.Builder lootTableBuilder = LootTable.builder().addLootPool(builder);
            lootTables.put(blockProvider.asBlock(), lootTableBuilder);
        }

        public void blockRetainingInventory(IBlockProvider blockProvider) {
            String name = Objects.requireNonNull(blockProvider.asBlock().getRegistryName()).getPath();
            LootPool.Builder builder = LootPool.builder()
                    .name(name)
                    .rolls(ConstantRange.of(1))
                    .addEntry(ItemLootEntry.builder(blockProvider.asBlock())
                            .acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY))
                            .acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY)
                                    .addOperation("inv", "BlockEntityTag.inv", CopyNbt.Action.REPLACE))
                            .acceptFunction(SetContents.builderIn()
                                    .addLootEntry(DynamicLootEntry.func_216162_a(new ResourceLocation("minecraft", "contents"))))
                    );

            LootTable.Builder lootTableBuilder =  LootTable.builder().addLootPool(builder);
            lootTables.put(blockProvider.asBlock(), lootTableBuilder);
        }
    }

    // endregion
}