package com.thenatekirby.babel.network.sync;

import com.thenatekirby.babel.core.api.IRedstoneModeProvider;
import com.thenatekirby.babel.core.api.ISyncable;
import com.thenatekirby.babel.machine.config.RedstoneMode;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class SyncableRedstoneMode implements ISyncable, IRedstoneModeProvider {
    private RedstoneMode redstoneMode;

    private SyncableRedstoneMode(RedstoneMode redstoneMode) {
        this.redstoneMode = redstoneMode;
    }

    public static SyncableRedstoneMode from(@Nonnull RedstoneMode redstoneMode) {
        return new SyncableRedstoneMode(redstoneMode);
    }

    @Override
    public RedstoneMode getRedstoneMode() {
        return redstoneMode;
    }

    @Override
    public void write(@Nonnull FriendlyByteBuf packetBuffer) {
        redstoneMode.write(packetBuffer);
    }

    @Override
    public void read(@Nonnull FriendlyByteBuf packetBuffer) {
        this.redstoneMode.read(packetBuffer);
    }
}
