package com.thenatekirby.babel.registration;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

// ====---------------------------------------------------------------------------====

public class DeferredEntity<T extends Entity> {
    private final RegistryObject<EntityType<T>> entityType;

    private DeferredEntity(String name, EntityType.Builder<T> builder, DeferredRegister<EntityType<?>> entityRegister) {
        this.entityType = entityRegister.register(name, () -> builder.build(name));
    }

    public static <T extends Entity> DeferredEntity<T> create(String name, EntityType.Builder<T> builder, DeferredRegister<EntityType<?>> entityRegister) {
        return new DeferredEntity<>(name, builder, entityRegister);
    }

    // ====---------------------------------------------------------------------------====
    // region Getters

    public EntityType<T> getAsEntityType() {
        return entityType.get();
    }

    // endregion
}
