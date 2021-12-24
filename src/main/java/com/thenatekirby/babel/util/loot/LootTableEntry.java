package com.thenatekirby.babel.util.loot;

import net.minecraft.world.item.ItemStack;

// ====---------------------------------------------------------------------------====

public class LootTableEntry {
    public int minDrop, maxDrop;
    public ItemStack item, smeltedItem;
    public float chance;
    public boolean enchanted;
    private float sortIndex;
}
