package com.thenatekirby.babel.mixin;

import net.minecraft.item.Item;
import net.minecraft.loot.ItemLootEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemLootEntry.class)
public interface ItemLootEntryMixin {
    @Accessor("item")
    Item getItem();
}
