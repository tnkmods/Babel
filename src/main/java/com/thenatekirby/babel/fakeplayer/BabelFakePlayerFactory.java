package com.thenatekirby.babel.fakeplayer;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    @Nullable
    private BabelFakePlayer getExistingFakePlayer(@Nonnull World world) {
        return storage.get(world.dimensionType());
    }

    @Nonnull
    private BabelFakePlayer makeFakePlayer(@Nonnull ServerWorld world) {
        return new BabelFakePlayer(world, playerUUID, playerName);
    }

    @Nonnull
    public BabelFakePlayer getFakePlayer(@Nonnull ServerWorld world) {
        BabelFakePlayer player = getExistingFakePlayer(world);
        if (player != null) {
            return player;
        }

        player = makeFakePlayer(world);
        storage.put(world.dimensionType(), player);
        return player;
    }

    public BabelFakePlayer getFakePlayer(@Nonnull ServerWorld world, @Nonnull BlockPos blockPos) {
        BabelFakePlayer fakePlayer = getFakePlayer(world);
        fakePlayer.absMoveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 90, 90);
        return fakePlayer;
    }
}
