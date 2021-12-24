package com.thenatekirby.babel.util.loot;

import com.thenatekirby.babel.mixin.LootItemMixin;
import com.thenatekirby.babel.mixin.LootPoolSingletonContainerMixin;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

// ====---------------------------------------------------------------------------====

public class LootEntryUtil {
    public static ItemLike getItem(LootPoolEntryContainer entry) {
        if (!(entry instanceof LootItem)) {
            return Items.AIR;
        }

        return ((LootItemMixin) entry).getItem();
    }

    public static LootItemFunction[] getFunctions(LootPoolEntryContainer entry) {
        if (!(entry instanceof LootPoolSingletonContainer)) {
            return new LootItemFunction[]{};
        }

        return ((LootPoolSingletonContainerMixin) entry).getFunctions();
    }
}
