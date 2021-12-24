package com.thenatekirby.babel.machine.config;

import com.thenatekirby.babel.capability.item.BabelSlotItemHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ====---------------------------------------------------------------------------====

@SuppressWarnings({"rawtypes", "unchecked"})
public class SlotConfiguration<T extends SlotConfiguration> {
    private final List<BabelSlotItemHandler> inputItemSlots = new ArrayList<>();
    private final List<BabelSlotItemHandler> outputItemSlots = new ArrayList<>();
    private final List<BabelSlotItemHandler> auxilaryItemSlots = new ArrayList<>();
    private EnergyBuffer energyBuffer = EnergyBuffer.ZERO;

    protected SlotConfiguration() {
    }

    // ====---------------------------------------------------------------------------====
    // region Factories

    public static <T extends SlotConfiguration> SlotConfiguration<T> make() {
        return new SlotConfiguration();
    }

    public T withInputs(BabelSlotItemHandler... itemSlots) {
        inputItemSlots.addAll(Arrays.asList(itemSlots));
        return (T) this;
    }

    public T withOutputs(BabelSlotItemHandler... itemSlots) {
        outputItemSlots.addAll(Arrays.asList(itemSlots));
        return (T) this;
    }

    public T withEnergyBuffer(EnergyBuffer buffer) {
        this.energyBuffer = buffer;
        return (T) this;
    }

    public T withAuxiliaries(BabelSlotItemHandler... itemSlots) {
        auxilaryItemSlots.addAll(Arrays.asList(itemSlots));
        return (T) this;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Getters

    public List<BabelSlotItemHandler> getInputItemSlots() {
        return inputItemSlots;
    }

    public List<BabelSlotItemHandler> getOutputItemSlots() {
        return outputItemSlots;
    }

    public List<BabelSlotItemHandler> getAuxilaryItemSlots() {
        return auxilaryItemSlots;
    }

    public EnergyBuffer getEnergyBuffer() {
        return energyBuffer;
    }

    // endregion
}
