package com.thenatekirby.babel;

import com.thenatekirby.babel.core.capability.CapabilityExperience;
import com.thenatekirby.babel.mod.BabelPackets;
import com.thenatekirby.babel.mod.BabelSerializers;
import com.thenatekirby.babel.core.MutableResourceLocation;
import com.thenatekirby.babel.network.NetworkManager;
import com.thenatekirby.babel.registration.BabelMod;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

// ====---------------------------------------------------------------------------====

@Mod("babel")
public class Babel extends BabelMod {
    private static final Logger LOGGER = LogManager.getLogger();
    public static Logger getLogger() {
        return LOGGER;
    }

    public static final String MOD_ID = "babel";
    public static final MutableResourceLocation MOD = new MutableResourceLocation(MOD_ID);
    public static NetworkManager NETWORK = new NetworkManager(Babel.MOD.withPath("channel"));

    public Babel() {
        setLifecycleAdapter(new ILifecycleAdapter() {
            @Override
            public void onSetupRegistries(BabelRegistryBuilder builder) {
                builder.addRecipeSerializers(BabelSerializers.RECIPE_SERIALIZERS);
            }

            @Override
            public void onRegisterCapabilities() {
                CapabilityExperience.register();
            }

            @Override
            public void onRegisterPackets() {
                BabelPackets.registerMessages();
            }
        });
    }
}
