package com.thenatekirby.babel.core;

import net.minecraft.item.ItemStack;

public class LootTableEntry {
    public int minDrop, maxDrop;
    public ItemStack item, smeltedItem;
    public float chance;
    public boolean enchanted;
    private float sortIndex;

}
