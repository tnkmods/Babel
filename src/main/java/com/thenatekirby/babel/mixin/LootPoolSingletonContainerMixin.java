package com.thenatekirby.babel.mixin;

import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// ====---------------------------------------------------------------------------====

@Mixin(LootPoolSingletonContainer.class)
public interface LootPoolSingletonContainerMixin {
    @Accessor("functions")
    LootItemFunction[] getFunctions();

    @Accessor("weight")
    int getWeight();
}
