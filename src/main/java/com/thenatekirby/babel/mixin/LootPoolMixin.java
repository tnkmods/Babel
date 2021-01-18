package com.thenatekirby.babel.mixin;

import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.conditions.ILootCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LootPool.class)
public interface LootPoolMixin {
    @Accessor("lootEntries")
    List<LootEntry> getLootEntries();

    @Accessor("conditions")
    List<ILootCondition> getConditions();
}
