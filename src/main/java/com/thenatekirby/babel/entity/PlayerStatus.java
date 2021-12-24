package com.thenatekirby.babel.entity;

import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;

public class PlayerStatus {
    private final Player player;

    private PlayerStatus(@Nonnull Player player) {
        this.player = player;
    }

    @Nonnull
    public static PlayerStatus of(@Nonnull Player player) {
        return new PlayerStatus(player);
    }

    // ====---------------------------------------------------------------------------====
    // region Abilities

    private Abilities getAbilities() {
        return player.getAbilities();
    }

    public boolean canBuild() {
        return getAbilities().mayBuild;
    }

    public boolean canFly() {
        return getAbilities().mayfly;
    }

    public boolean isCreative() {
        return getAbilities().instabuild;
    }

    public boolean isFlying() {
        return getAbilities().flying;
    }

    public boolean isInvulnerable() {
        return getAbilities().invulnerable;
    }

    // endregion
}
