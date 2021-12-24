package com.thenatekirby.babel.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.core.api.IBlockProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// ====---------------------------------------------------------------------------====

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
    protected void validate(@Nonnull Map<ResourceLocation, LootTable> map, @Nonnull ValidationContext validationContext) {
    }

    @Override
    public void run(@Nonnull HashCache cache) {
        addBlockLootTables(new Builder());

        Map<ResourceLocation, LootTable> tables = new HashMap<>();
        for (Map.Entry<Block, LootTable.Builder> entry : lootTables.entrySet()) {
            tables.put(entry.getKey().getLootTable(), entry.getValue().setParamSet(LootContextParamSets.BLOCK).build());
        }

        writeLootTables(cache, tables);
    }

    private void writeLootTables(@Nonnull HashCache cache, @Nonnull Map<ResourceLocation, LootTable> tables) {
        Path outputFolder = dataGenerator.getOutputFolder();

        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");

            try {
                DataProvider.save(GSON, cache, LootTables.serialize(lootTable), path);

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
            LootPool.Builder builder = LootPool.lootPool()
                    .name(name)
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(blockProvider.asBlock()));

            LootTable.Builder lootTableBuilder = LootTable.lootTable().withPool(builder);
            lootTables.put(blockProvider.asBlock(), lootTableBuilder);
        }

        public void blockRetainingInventory(IBlockProvider blockProvider) {
            String name = Objects.requireNonNull(blockProvider.asBlock().getRegistryName()).getPath();
            LootPool.Builder builder = LootPool.lootPool()
                    .name(name)
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(blockProvider.asBlock())
                            .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                            .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                    .copy("inv", "BlockEntityTag.inv", CopyNbtFunction.MergeStrategy.REPLACE))
                            // TODO: Fixme
//                            .apply(SetContainerContents.setContents()
//                                    .withEntry(DynamicLootEntry.dynamicEntry(new ResourceLocation("minecraft", "contents"))))
                    );

            LootTable.Builder lootTableBuilder =  LootTable.lootTable().withPool(builder);
            lootTables.put(blockProvider.asBlock(), lootTableBuilder);
        }
    }

    // endregion
}
