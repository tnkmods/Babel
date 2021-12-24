package com.thenatekirby.babel.registration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

// ====---------------------------------------------------------------------------====

public class DeferredMenu<T extends AbstractContainerMenu> {
    private final RegistryObject<MenuType<T>> container;

    private DeferredMenu(String name, IDeferredContainerFactory<T> factory, DeferredRegister<MenuType<?>> containerRegister) {
        this.container = containerRegister.register(name, () -> IForgeMenuType.create(factory.makeContainerFactory()));
    }

    public static <T extends AbstractContainerMenu> DeferredMenu<T> create(String name, IDeferredContainerFactory<T> factory, DeferredRegister<MenuType<?>> containerRegister) {
        return new DeferredMenu<>(name, factory, containerRegister);
    }

    // ====---------------------------------------------------------------------------====
    // Getters

    public MenuType<T> getMenuType() {
        return container.get();
    }

    // ====---------------------------------------------------------------------------====
    // Factory

    public interface IDeferredContainerFactory<T extends AbstractContainerMenu> {
        T create(int windowId, Level level, BlockPos pos, Inventory playerInventory, Player player);

        default IContainerFactory<T> makeContainerFactory() {
            final IDeferredContainerFactory<T> factory = this;

            return ((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                Level world = inv.player.level;
                return factory.create(windowId, world, pos, inv, inv.player);
            });
        }
    }
}
