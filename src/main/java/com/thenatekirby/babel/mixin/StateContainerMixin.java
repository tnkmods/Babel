package com.thenatekirby.babel.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StateContainer.class)
public interface StateContainerMixin<O> {
    @Accessor("owner")
    void setOwner(O owner);
}
