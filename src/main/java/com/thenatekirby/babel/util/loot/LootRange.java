package com.thenatekirby.babel.util.loot;

import com.thenatekirby.babel.mixin.ConstantValueMixin;
import com.thenatekirby.babel.mixin.UniformGeneratorMixin;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class LootRange {
    public static final LootRange ZERO = new LootRange(0, 0);
    private final float min;
    private final float max;

    public LootRange(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public static LootRange from(UniformGenerator generator) {
        NumberProvider minProvider = ((UniformGeneratorMixin) generator).getUniformMin();
        NumberProvider maxProvider = ((UniformGeneratorMixin) generator).getUniformMax();
        if (!(minProvider instanceof ConstantValue minValue) || !(maxProvider instanceof ConstantValue maxValue)) {
            return ZERO;
        }

        float min = ((ConstantValueMixin) (Object) minValue).getValue();
        float max = ((ConstantValueMixin) (Object) maxValue).getValue();

        return new LootRange(min, max);
    }
}
