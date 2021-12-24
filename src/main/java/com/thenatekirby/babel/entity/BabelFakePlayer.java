package com.thenatekirby.babel.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;
import java.util.UUID;

// ====---------------------------------------------------------------------------====

public class BabelFakePlayer extends FakePlayer {
    BabelFakePlayer(@Nonnull ServerLevel world, @Nonnull UUID playerUuid, @Nonnull String name) {
        super(world, new GameProfile(playerUuid, name));
    }
}
