package com.thenatekirby.babel.mixin;

import net.minecraft.state.StateHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StateHolder.class)
public interface StateHolderMixin<O> {
    @Accessor("instance")
    void setInstance(O instance);
}
