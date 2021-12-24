package com.thenatekirby.babel;

import com.thenatekirby.babel.babelmod.BabelPackets;
import com.thenatekirby.babel.babelmod.BabelSerializers;
import com.thenatekirby.babel.capability.experience.CapabilityExperience;
import com.thenatekirby.babel.core.BabelMod;
import com.thenatekirby.babel.core.MutableResourceLocation;
import com.thenatekirby.babel.core.lifecycle.IModLifecycleAdapter;
import com.thenatekirby.babel.core.lifecycle.RegistryBuilder;
import com.thenatekirby.babel.network.NetworkManager;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

@Mod("babel")
public class Babel extends BabelMod {
    public static final String MOD_ID = "babel";
    public static final MutableResourceLocation MOD = new MutableResourceLocation(MOD_ID);
    public static NetworkManager NETWORK = new NetworkManager(Babel.MOD.withPath("channel"));

    public Babel() {
        setModLifecycleAdapter(new IModLifecycleAdapter() {
            @Override
            public void onSetupRegistries(@Nonnull RegistryBuilder builder) {
                builder.addRecipeSerializers(BabelSerializers.RECIPE_SERIALIZERS);
            }

            @Override
            public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
                CapabilityExperience.register(event);
            }

            @Override
            public void onRegisterPackets() {
                BabelPackets.registerMessages();
            }
        });
    }
}
