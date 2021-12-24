package com.thenatekirby.babel.mixin;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// ====---------------------------------------------------------------------------====

@Mixin(LootPool.class)
public interface LootPoolMixin {
    @Accessor("entries")
    LootPoolEntryContainer[] getLootEntries();

    @Accessor("conditions")
    LootItemCondition[] getConditions();
}
