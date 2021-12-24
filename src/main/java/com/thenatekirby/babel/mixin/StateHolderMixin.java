package com.thenatekirby.babel.mixin;

import net.minecraft.world.level.block.state.StateHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StateHolder.class)
public interface StateHolderMixin<O> {
    @Accessor("owner")
    void setOwner(O owner);
}
