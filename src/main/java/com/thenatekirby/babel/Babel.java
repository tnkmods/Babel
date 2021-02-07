package com.thenatekirby.babel;

import com.thenatekirby.babel.core.capability.CapabilityExperience;
import com.thenatekirby.babel.mod.BabelPackets;
import com.thenatekirby.babel.mod.BabelSerializers;
import com.thenatekirby.babel.core.MutableResourceLocation;
import com.thenatekirby.babel.network.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// ====---------------------------------------------------------------------------====

@Mod("babel")
public class Babel {
    private static final Logger LOGGER = LogManager.getLogger();
    public static Logger getLogger() {
        return LOGGER;
    }

    public static final String MOD_ID = "babel";
    public static final MutableResourceLocation MOD = new MutableResourceLocation(MOD_ID);
    public static NetworkManager NETWORK = new NetworkManager(Babel.MOD.withPath("channel"));

    public Babel() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        BabelSerializers.register();
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        CapabilityExperience.register();

        BabelPackets.registerMessages();
    }
}
