package com.thenatekirby.babel.util.loot;

import com.thenatekirby.babel.mixin.EnchantRandomlyFunctionMixin;
import com.thenatekirby.babel.mixin.SetItemCountFunctionMixin;
import com.thenatekirby.babel.mixin.SetNbtFunctionMixin;
import com.thenatekirby.babel.mixin.SetPotionFunctionMixin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import javax.annotation.Nonnull;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class LootFunctionUtil {
    @Nonnull
    public static List<Enchantment> getEnchantments(@Nonnull EnchantRandomlyFunction function) {
        return ((EnchantRandomlyFunctionMixin) function).getEnchantments();
    }

    @Nonnull
    public static CompoundTag getTag(@Nonnull SetNbtFunction function) {
        return ((SetNbtFunctionMixin) function).getTag();
    }

    @Nonnull
    public static NumberProvider getValue(@Nonnull SetItemCountFunction function) {
        return ((SetItemCountFunctionMixin) function).getValue();
    }

    @Nonnull
    public static Potion getPotion(@Nonnull SetPotionFunction function) {
        return ((SetPotionFunctionMixin) function).getPotion();
    }
}
