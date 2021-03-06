package com.thenatekirby.babel.registration;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;

public class DeferredContainer<T extends Container> {
    private final RegistryObject<ContainerType<T>> container;

    private DeferredContainer(String name, IDeferredContainerFactory<T> factory, DeferredRegister<ContainerType<?>> containerRegister) {
        this.container = containerRegister.register(name, () -> IForgeContainerType.create(factory.makeContainerFactory()));
    }

    public static <T extends Container> DeferredContainer<T> create(String name, IDeferredContainerFactory<T> factory, DeferredRegister<ContainerType<?>> containerRegister) {
        return new DeferredContainer<>(name, factory, containerRegister);
    }

    // ====---------------------------------------------------------------------------====
    // Getters

    public ContainerType<T> getContainerType() {
        return container.get();
    }

    // ====---------------------------------------------------------------------------====
    // Factory

    public interface IDeferredContainerFactory<T extends Container> {
        T create(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player);

        default IContainerFactory<T> makeContainerFactory() {
            final IDeferredContainerFactory<T> factory = this;

            return ((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.world;
                return factory.create(windowId, world, pos, inv, inv.player);
            });
        }
    }
}
