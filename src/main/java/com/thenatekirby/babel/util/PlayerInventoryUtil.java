package com.thenatekirby.babel.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class PlayerInventoryUtil {

    // region Held Items
    public static ItemStack getHeldItem(@Nonnull Player player) {
        InteractionHand interactionHand = player.getUsedItemHand();
        return player.getItemInHand(interactionHand);
    }

    public static ItemStack getItemInHand(@Nonnull Player player, InteractionHand hand) {
        return player.getItemInHand(hand);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Manipulation

    public static boolean addToPlayerInventory(Player entity, ItemStack itemStack) {
        return entity.getInventory().add(itemStack);
    }

    public static void addToPlayerInventoryOrDrop(Player entity, ItemStack itemStack) {
        if (!addToPlayerInventory(entity, itemStack)) {
            entity.drop(itemStack, true);
        }
    }

    // endregion
}
