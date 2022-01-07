package com.thenatekirby.babel.entity.wanderingtrader;

import com.thenatekirby.babel.util.ItemStackUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// ====---------------------------------------------------------------------------====

public class WanderingTraderTrades {
//    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> WANDERING_TRADER_TRADES = toIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{
//            new VillagerTrades.ItemsForEmeralds(Items.SEA_PICKLE, 2, 1, 5, 1), new VillagerTrades.ItemsForEmeralds(Items.SLIME_BALL, 4, 1, 5, 1), new VillagerTrades.ItemsForEmeralds(Items.GLOWSTONE, 2, 1, 5, 1), new VillagerTrades.ItemsForEmeralds(Items.NAUTILUS_SHELL, 5, 1, 5, 1), new VillagerTrades.ItemsForEmeralds(Items.FERN, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.SUGAR_CANE, 1, 1, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.PUMPKIN, 1, 1, 4, 1), new VillagerTrades.ItemsForEmeralds(Items.KELP, 3, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.CACTUS, 3, 1, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.DANDELION, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.POPPY, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.BLUE_ORCHID, 1, 1, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.ALLIUM, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.AZURE_BLUET, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.RED_TULIP, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.ORANGE_TULIP, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.WHITE_TULIP, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.PINK_TULIP, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.OXEYE_DAISY, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.CORNFLOWER, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.LILY_OF_THE_VALLEY, 1, 1, 7, 1), new VillagerTrades.ItemsForEmeralds(Items.WHEAT_SEEDS, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.BEETROOT_SEEDS, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.PUMPKIN_SEEDS, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.MELON_SEEDS, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.ACACIA_SAPLING, 5, 1, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.BIRCH_SAPLING, 5, 1, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.DARK_OAK_SAPLING, 5, 1, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.JUNGLE_SAPLING, 5, 1, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.OAK_SAPLING, 5, 1, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.SPRUCE_SAPLING, 5, 1, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.RED_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.WHITE_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.BLUE_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.PINK_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.BLACK_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.GREEN_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.LIGHT_GRAY_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.MAGENTA_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.YELLOW_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.GRAY_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.PURPLE_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.LIGHT_BLUE_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.LIME_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.ORANGE_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.BROWN_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.CYAN_DYE, 1, 3, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.BRAIN_CORAL_BLOCK, 3, 1, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.BUBBLE_CORAL_BLOCK, 3, 1, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.FIRE_CORAL_BLOCK, 3, 1, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.HORN_CORAL_BLOCK, 3, 1, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.TUBE_CORAL_BLOCK, 3, 1, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.VINE, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.BROWN_MUSHROOM, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.RED_MUSHROOM, 1, 1, 12, 1), new VillagerTrades.ItemsForEmeralds(Items.LILY_PAD, 1, 2, 5, 1), new VillagerTrades.ItemsForEmeralds(Items.SMALL_DRIPLEAF, 1, 2, 5, 1), new VillagerTrades.ItemsForEmeralds(Items.SAND, 1, 8, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.RED_SAND, 1, 4, 6, 1), new VillagerTrades.ItemsForEmeralds(Items.POINTED_DRIPSTONE, 1, 2, 5, 1), new VillagerTrades.ItemsForEmeralds(Items.ROOTED_DIRT, 1, 2, 5, 1), new VillagerTrades.ItemsForEmeralds(Items.MOSS_BLOCK, 1, 2, 5, 1)}
//            , 2, new VillagerTrades.ItemListing[]{new VillagerTrades.ItemsForEmeralds(Items.TROPICAL_FISH_BUCKET, 5, 1, 4, 1), new VillagerTrades.ItemsForEmeralds(Items.PUFFERFISH_BUCKET, 5, 1, 4, 1), new VillagerTrades.ItemsForEmeralds(Items.PACKED_ICE, 3, 1, 6, 1), new VillagerTrades.ItemsForEmeralds(Items.BLUE_ICE, 6, 1, 6, 1), new VillagerTrades.ItemsForEmeralds(Items.GUNPOWDER, 1, 1, 8, 1), new VillagerTrades.ItemsForEmeralds(Items.PODZOL, 3, 3, 6, 1)
//    }));

    private static List<WanderingTraderTrade> tradesFromItemListings(WanderingTrader trader, Random random, VillagerTrades.ItemListing[] listings) {
        return Arrays.stream(listings).map(trade -> {
            MerchantOffer offer = trade.getOffer(trader, random);
            if (offer != null) {
                return new WanderingTraderTrade(ItemStackUtil.itemStackWithSize(offer.getResult(), 1));
            } else {
                return WanderingTraderTrade.empty();
            }
        }).toList();
    }

    public static List<WanderingTraderTrade> getTrades(@Nonnull Level level) {
        WanderingTrader trader = new WanderingTrader(EntityType.WANDERING_TRADER, level);
        Int2ObjectMap<VillagerTrades.ItemListing[]> trades = VillagerTrades.WANDERING_TRADER_TRADES;

        List<WanderingTraderTrade> results = new ArrayList<>();
        trades.forEach((idx, listing) -> results.addAll(tradesFromItemListings(trader, level.random, listing)));

        return results;
    }
}
