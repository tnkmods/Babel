package com.thenatekirby.babel.network.sync;

import com.thenatekirby.babel.api.IProgress;
import com.thenatekirby.babel.api.ISyncable;
import com.thenatekirby.babel.core.capability.ExperienceStorage;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;

public class SyncableExperience implements ISyncable {
    private ExperienceStorage experienceStorage;

    private SyncableExperience(@Nonnull ExperienceStorage experienceStorage) {
        this.experienceStorage = experienceStorage;
    }

    public static SyncableExperience from(@Nonnull ExperienceStorage experienceStorage) {
        return new SyncableExperience(experienceStorage);
    }

    @Override
    public void write(@Nonnull PacketBuffer packetBuffer) {
        packetBuffer.writeInt(experienceStorage.getExperienceTotal());
    }

    @Override
    public void read(@Nonnull PacketBuffer packetBuffer) {
        experienceStorage.setExperienceTotal(packetBuffer.readInt());
    }

    public ExperienceStorage getExperienceStorage() {
        return experienceStorage;
    }

    public IProgress getLevelProgress() {
        return experienceStorage.getLevelProgress();
    }

    public IProgress getTotalProgress() {
        return experienceStorage.getTotalProgress();
    }
}
