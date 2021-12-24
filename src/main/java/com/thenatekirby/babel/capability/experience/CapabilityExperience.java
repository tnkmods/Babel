package com.thenatekirby.babel.capability.experience;

import com.thenatekirby.babel.core.api.IExperienceStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.energy.IEnergyStorage;

// ====---------------------------------------------------------------------------====

public class CapabilityExperience {
    public static final Capability<IEnergyStorage> EXPERIENCE = CapabilityManager.get(new CapabilityToken<>(){});;

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IExperienceStorage.class);
    }
}
