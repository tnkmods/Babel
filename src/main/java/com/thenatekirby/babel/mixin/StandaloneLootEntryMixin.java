package com.thenatekirby.babel.mixin;

import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StandaloneLootEntry.class)
public interface StandaloneLootEntryMixin {
    @Accessor("functions")
    ILootFunction[] getFunctions();

    @Accessor("weight")
    int getWeight();
}
