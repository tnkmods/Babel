package com.thenatekirby.babel.core.lifecycle;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public interface IModLifecycleAdapter {
    // region Config
    default void onRegisterConfig() {
    }

    default void onLoadConfig() {
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region DeferredRegisters

    default void onSetupRegistries(@Nonnull RegistryBuilder builder) {
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Registration

    default void onRegisterRecipeCollections() {
    }

    default void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
    }

    default void onRegisterPackets() {
    }

    default void onRegisterScreens() {
    }

    default void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region FML Lifecycle

    default void onServerStarting(ServerStartingEvent event) {
    }

    default void onClientSetup(FMLClientSetupEvent event) {
    }

    default void onCommonSetup(FMLCommonSetupEvent event) {
    }

    // endregion
}
