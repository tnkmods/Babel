package com.thenatekirby.babel.registration;

import com.thenatekirby.babel.core.api.IItemProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

// ====---------------------------------------------------------------------------====

public class DeferredItem<T extends Item> implements IItemProvider {
    private final RegistryObject<T> item;

    private DeferredItem(String name, Supplier<T> supplier, DeferredRegister<Item> register) {
        item = register.register(name, supplier);
    }

    public static<T extends Item> DeferredItem<T> create(String name, Supplier<T> supplier, DeferredRegister<Item> register) {
        return new DeferredItem<>(name, supplier, register);
    }

    // ====---------------------------------------------------------------------------====
    // region Getters

    @Nonnull
    @Override
    public T asItem() {
        return item.get();
    }

    public T toItem() {
        return asItem();
    }

    // endregion
}
