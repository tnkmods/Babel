package com.thenatekirby.babel.core;

import com.thenatekirby.babel.core.lifecycle.IModLifecycleAdapter;
import com.thenatekirby.babel.core.lifecycle.RegistryBuilder;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Optional;

// ====---------------------------------------------------------------------------====

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class BabelMod {
    private static final Logger LOGGER = LogManager.getLogger();
    public static Logger getLogger() {
        return LOGGER;
    }

    private Optional<IModLifecycleAdapter> lifecycleAdapter;

    public BabelMod() {
        lifecycleAdapter = Optional.empty();
    }

    public void setModLifecycleAdapter(@Nonnull IModLifecycleAdapter lifecycleAdapter) {
        this.lifecycleAdapter = Optional.of(lifecycleAdapter);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        lifecycleAdapter.onSetupRegistries(new RegistryBuilder());
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        lifecycleAdapter.ifPresent(adapter -> {
            adapter.onClientSetup(event);
            adapter.onRegisterScreens();
        });
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        lifecycleAdapter.ifPresent(adapter -> {
            adapter.onCommonSetup(event);
            adapter.onRegisterPackets();
        });
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        lifecycleAdapter.ifPresent(adapter -> {
            adapter.onServerStarting(event);
        });
    }

    @SubscribeEvent
    public void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        lifecycleAdapter.ifPresent(adapter -> adapter.onRegisterEntityRenderers(event));
    }

    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        lifecycleAdapter.ifPresent(adapter -> adapter.onRegisterCapabilities(event));
    }
}
