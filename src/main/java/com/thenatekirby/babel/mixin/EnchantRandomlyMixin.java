package com.thenatekirby.babel.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.loot.functions.EnchantRandomly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(EnchantRandomly.class)
public interface EnchantRandomlyMixin {
    @Accessor("enchantments")
    List<Enchantment> getEnchantments();
}
