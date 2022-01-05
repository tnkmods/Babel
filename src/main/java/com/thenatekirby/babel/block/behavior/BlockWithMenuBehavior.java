package com.thenatekirby.babel.block.behavior;

import com.thenatekirby.babel.machine.entity.BabelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class BlockWithMenuBehavior {
    @Nonnull
    public static InteractionResult use(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull Component displayName, @Nonnull IMenuConstructor menuConstructor) {
        if (!level.isClientSide) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof BabelBlockEntity) {
                MenuProvider provider = new MenuProvider() {
                    @Override
                    @Nonnull
                    public Component getDisplayName() {
                        return displayName;
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int windowId, @Nonnull Inventory inventory, @Nonnull Player player) {
                        return menuConstructor.create(windowId, level, pos, inventory, player);
                    }
                };

                NetworkHooks.openGui((ServerPlayer) player, provider, tileEntity.getBlockPos());
            }
        }

        return InteractionResult.SUCCESS;
    }

    public interface IMenuConstructor {
        AbstractContainerMenu create(int windowId, @Nonnull Level level, @Nonnull BlockPos blockPos, @Nonnull Inventory inventory, @Nonnull Player player);
    }
}
