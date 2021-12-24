package com.thenatekirby.babel.mixin;

import net.minecraft.world.ContainerListener;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

// ====---------------------------------------------------------------------------====

@Mixin(AbstractContainerMenu.class)
public interface AbstractContainerMenuMixin {
    @Accessor("containerListeners")
    List<ContainerListener> getContainerListeners();
}
