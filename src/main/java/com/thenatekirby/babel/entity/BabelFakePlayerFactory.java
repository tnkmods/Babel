package com.thenatekirby.babel.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// ====---------------------------------------------------------------------------====

public class BabelFakePlayerFactory {
    private final Map<DimensionType, BabelFakePlayer> storage = new HashMap<>();
    private final UUID playerUUID;
    private final String playerName;

    public BabelFakePlayerFactory(@Nonnull UUID playerUUID, @Nonnull String playerName) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
    }

    public BabelFakePlayerFactory(@Nonnull String playerUUID, @Nonnull String playerName) {
        this.playerUUID = UUID.fromString(playerUUID);
        this.playerName = playerName;
    }

    // ====---------------------------------------------------------------------------====
    // region Lookup

    @Nullable
    private BabelFakePlayer getExistingFakePlayer(@Nonnull Level level) {
        return storage.get(level.dimensionType());
    }

    @Nonnull
    public BabelFakePlayer getFakePlayer(@Nonnull ServerLevel level) {
        BabelFakePlayer player = getExistingFakePlayer(level);
        if (player != null) {
            return player;
        }

        player = makeFakePlayer(level);
        storage.put(level.dimensionType(), player);
        return player;
    }

    public BabelFakePlayer getFakePlayer(@Nonnull ServerLevel world, @Nonnull BlockPos blockPos) {
        BabelFakePlayer fakePlayer = getFakePlayer(world);
        fakePlayer.absMoveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 90, 90);
        return fakePlayer;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Creation

    @Nonnull
    private BabelFakePlayer makeFakePlayer(@Nonnull ServerLevel level) {
        return new BabelFakePlayer(level, playerUUID, playerName);
    }

    // endregion
}
