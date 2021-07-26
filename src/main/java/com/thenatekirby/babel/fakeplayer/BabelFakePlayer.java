package com.thenatekirby.babel.fakeplayer;

import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;
import java.util.UUID;

@SuppressWarnings("EntityConstructor")
public class BabelFakePlayer extends FakePlayer {
    BabelFakePlayer(ServerWorld world, @Nonnull UUID playerUuid, @Nonnull String name) {
        super(world, new GameProfile(playerUuid, name));
    }

    @Override
    protected void playEquipSound(ItemStack stack) {
    }
}
