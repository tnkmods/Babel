package com.thenatekirby.babel.mixin;

import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.functions.SetCount;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SetCount.class)
public interface SetCountMixin {
    @Accessor("value")
    IRandomRange getCountRange();
}
