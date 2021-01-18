package com.thenatekirby.babel.loot;

import com.thenatekirby.babel.mixin.ItemLootEntryMixin;
import com.thenatekirby.babel.mixin.StandaloneLootEntryMixin;
import net.minecraft.item.Items;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nullable;

public class LootEntryUtil {
    public static IItemProvider getItem(LootEntry entry) {
        if (!(entry instanceof ItemLootEntry)) {
            return Items.AIR;
        }

        return ((ItemLootEntryMixin) entry).getItem();
    }

    public static ILootFunction[] getFunctions(LootEntry entry) {
        if (!(entry instanceof StandaloneLootEntry)) {
            return new ILootFunction[]{};
        }

        return ((StandaloneLootEntryMixin) entry).getFunctions();
    }
}
