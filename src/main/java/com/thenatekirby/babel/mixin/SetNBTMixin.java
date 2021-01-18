package com.thenatekirby.babel.mixin;

import net.minecraft.loot.functions.SetNBT;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SetNBT.class)
public interface SetNBTMixin {
    @Accessor("tag")
    CompoundNBT getTag();
}
