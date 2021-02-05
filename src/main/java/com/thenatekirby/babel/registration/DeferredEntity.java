package com.thenatekirby.babel.registration;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

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

    public EntityType<T> getAsEntityType() {
        return entityType.get();
    }
}
