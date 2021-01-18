package com.thenatekirby.babel.loot;

import com.thenatekirby.babel.mixin.EnchantRandomlyMixin;
import com.thenatekirby.babel.mixin.SetNBTMixin;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.loot.functions.EnchantRandomly;
import net.minecraft.loot.functions.SetNBT;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class LootFunctionUtil {
    @Nonnull
    public static List<Enchantment> getEnchantments(@Nonnull EnchantRandomly function) {
        return ((EnchantRandomlyMixin) function).getEnchantments();
    }

    @Nonnull
    public static CompoundNBT getTag(@Nonnull SetNBT function) {
        return ((SetNBTMixin) function).getTag();
    }
}
