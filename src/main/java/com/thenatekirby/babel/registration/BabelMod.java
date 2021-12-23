package com.thenatekirby.babel.registration;

import com.thenatekirby.babel.Babel;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class BabelMod {
    private static final Logger LOGGER = LogManager.getLogger();
    public static Logger getLogger() {
        return LOGGER;
    }

    private Optional<ILifecycleAdapter> lifecycleAdapter;

    protected void setLifecycleAdapter(@Nullable ILifecycleAdapter lifecycleAdapter) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        this.lifecycleAdapter = Optional.ofNullable(lifecycleAdapter);
        if (lifecycleAdapter != null) {
            lifecycleAdapter.onSetupRegistries(new BabelRegistryBuilder());
        }
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
            adapter.onRegisterCapabilities();
            adapter.onRegisterPackets();
        });
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        lifecycleAdapter.ifPresent(adapter -> {
            adapter.onServerStarting(event);
        });
    }

    public static class BabelRegistryBuilder {
        public void addRecipeSerializers(DeferredRegister<IRecipeSerializer<?>> register) {
            register.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        public void addItems(DeferredRegister<Item> register) {
            register.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        public void addBlocks(DeferredRegister<Block> register) {
            register.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        public void addBlockEntities(DeferredRegister<TileEntityType<?>> register) {
            register.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        public void addContainers(DeferredRegister<ContainerType<?>> register) {
            register.register(FMLJavaModLoadingContext.get().getModEventBus());
        }
    }

    public interface ILifecycleAdapter {
        // region Config
        default void onRegisterConfig() {
        }

        default void onLoadConfig() {
        }
        // endregion
        // region DeferredRegisters
        default void onSetupRegistries(BabelRegistryBuilder builder) {
        }

        // endregion
        // region Registration
        default void onRegisterCapabilities() {
        }

        default void onRegisterPackets() {
        }

        default void onRegisterScreens() {
        }
        // endregion

        // region Lifecycle
        default void onServerStarting(FMLServerStartingEvent event) {
        }

        default void onClientSetup(FMLClientSetupEvent event) {
        }

        default void onCommonSetup(FMLCommonSetupEvent event) {
        }
        // endregion
    }
}
