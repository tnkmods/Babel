package com.thenatekirby.babel.mixin;

import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StateDefinition.class)
public class StateDefinitionMixin<O> {
    @Shadow @Final private O owner;
    public O mutableOwner;

    /**
     * @author thenatekirby
     */
    @Overwrite
    public O getOwner() {
        if (mutableOwner != null) {
            return mutableOwner;
        }

        return owner;
    }
}
