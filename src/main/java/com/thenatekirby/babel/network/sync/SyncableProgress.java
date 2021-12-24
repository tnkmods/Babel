package com.thenatekirby.babel.network.sync;

import com.thenatekirby.babel.core.api.ISyncable;
import com.thenatekirby.babel.core.progress.IProgress;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class SyncableProgress implements ISyncable, IProgress {
    private IProgress progress;
    private int min;
    private int max;
    private int current;

    public SyncableProgress(IProgress progress) {
        this.progress = progress;
        this.min = progress.getProgressMin();
        this.max = progress.getProgressMax();
        this.current = progress.getProgressCurrent();
    }

    public static SyncableProgress from(@Nonnull IProgress progress) {
        return new SyncableProgress(progress);
    }

    @Override
    public void write(@Nonnull FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(progress.getProgressMin());
        packetBuffer.writeInt(progress.getProgressMax());
        packetBuffer.writeInt(progress.getProgressCurrent());
    }

    @Override
    public void read(@Nonnull FriendlyByteBuf packetBuffer) {
        this.min = packetBuffer.readInt();
        this.max = packetBuffer.readInt();
        this.current = packetBuffer.readInt();
    }

    @Override
    public int getProgressMin() {
        return min;
    }

    @Override
    public int getProgressMax() {
        return max;
    }

    @Override
    public int getProgressCurrent() {
        return current;
    }
}
