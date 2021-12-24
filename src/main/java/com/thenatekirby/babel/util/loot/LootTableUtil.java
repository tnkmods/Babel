package com.thenatekirby.babel.util.loot;

import com.thenatekirby.babel.mixin.LootPoolMixin;
import com.thenatekirby.babel.mixin.LootTableMixin;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

// ====---------------------------------------------------------------------------====

public class LootTableUtil {
    public static List<LootPool> getLootTablePools(LootTable table) {
        return ((LootTableMixin) table).getPools();
    }

    public static LootPoolEntryContainer[] getLootTableEntries(LootPool pool) {
        return ((LootPoolMixin) pool).getLootEntries();
    }

    public static LootItemCondition[] getLootTableConditions(LootPool pool) {
        return ((LootPoolMixin) pool).getConditions();
    }
}
