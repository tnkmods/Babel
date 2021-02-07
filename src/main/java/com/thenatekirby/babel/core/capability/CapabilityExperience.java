package com.thenatekirby.babel.core.capability;

import com.thenatekirby.babel.api.IExperienceStorage;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityExperience {
    @CapabilityInject(IExperienceStorage.class)
    public static Capability<IExperienceStorage> EXPERIENCE = null;

    public CapabilityExperience() {
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IExperienceStorage.class, new Capability.IStorage<IExperienceStorage>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IExperienceStorage> capability, IExperienceStorage instance, Direction side) {
                return IntNBT.valueOf(instance.getExperienceStored());
            }

            @Override
            public void readNBT(Capability<IExperienceStorage> capability, IExperienceStorage instance, Direction side, INBT nbt) {
                ((ExperienceStorage) instance).setExperience(((IntNBT) nbt).getInt());
            }
        }, () -> new ExperienceStorage(1000, 0, 0, 0));
    }
}
