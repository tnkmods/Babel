package com.thenatekirby.babel.util;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class InventoryUtil {
    @OnlyIn(Dist.CLIENT)
    public static ItemStack getPlayerMouseHeldItemStack() {
        if (Minecraft.getInstance().player == null) {
            return ItemStack.EMPTY;
        }

        return Minecraft.getInstance().player.inventory.getItemStack();
    }
}
