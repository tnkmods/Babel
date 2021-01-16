package com.thenatekirby.babel.registration;

import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

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
    // Getters

    @Nonnull
    @Override
    public T asItem() {
        return item.get();
    }
}
