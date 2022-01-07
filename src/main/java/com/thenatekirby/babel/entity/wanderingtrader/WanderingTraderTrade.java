package com.thenatekirby.babel.entity.wanderingtrader;

import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

@SuppressWarnings("ClassCanBeRecord")
public class WanderingTraderTrade {
    private final ItemStack result;

    public WanderingTraderTrade(@Nonnull ItemStack result) {
        this.result = result;
    }

    @Nonnull
    public ItemStack getResult() {
        return result;
    }

    public boolean isEmpty() {
        return result.isEmpty();
    }

    public static WanderingTraderTrade empty() {
        return new WanderingTraderTrade(ItemStack.EMPTY);
    }
}
