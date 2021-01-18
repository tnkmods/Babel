package com.thenatekirby.babel.loot;

import com.thenatekirby.babel.mixin.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.EnchantRandomly;
import net.minecraft.loot.functions.SetNBT;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LootTableUtil {
    public static List<LootPool> getLootTablePools(LootTable table) {
        return ((LootTableMixin) table).getPools();
    }

    public static List<LootEntry> getLootTableEntries(LootPool pool) {
        return ((LootPoolMixin) pool).getLootEntries();
    }

    public static List<ILootCondition> getLootTableConditions(LootPool pool) {
        return ((LootPoolMixin) pool).getConditions();
    }
}
