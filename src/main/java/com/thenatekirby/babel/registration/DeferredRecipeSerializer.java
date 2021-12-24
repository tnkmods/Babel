package com.thenatekirby.babel.registration;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

// ====---------------------------------------------------------------------------====

public class DeferredRecipeSerializer<T extends RecipeSerializer<?>> {
    private final RegistryObject<T> registryObject;

    private DeferredRecipeSerializer(String name, Supplier<T> supplier, DeferredRegister<RecipeSerializer<?>> recipeRegister) {
        this.registryObject = recipeRegister.register(name, supplier);
    }

    public static <T extends RecipeSerializer<?>> DeferredRecipeSerializer<T> create(String name, Supplier<T> supplier, DeferredRegister<RecipeSerializer<?>> recipeRegister) {
        return new DeferredRecipeSerializer<T>(name, supplier, recipeRegister);
    }

    // ====---------------------------------------------------------------------------====
    // region Getters

    public T getAsRecipeSerializer() {
        return registryObject.get();
    }

    // endregion
}
