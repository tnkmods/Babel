package com.thenatekirby.babel.machine.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ====---------------------------------------------------------------------------====

@SuppressWarnings({"rawtypes", "unchecked"})
public class SlotConfiguration<T extends SlotConfiguration> {
    private final List<InventoryItemSlot> inputItemSlots = new ArrayList<>();
    private final List<InventoryItemSlot> outputItemSlots = new ArrayList<>();
    private final List<InventoryItemSlot> auxilaryItemSlots = new ArrayList<>();
    private EnergyBuffer energyBuffer = EnergyBuffer.ZERO;

    protected SlotConfiguration() {
    }

    // ====---------------------------------------------------------------------------====
    // region Factories

    public static <T extends SlotConfiguration> SlotConfiguration<T> make() {
        return new SlotConfiguration();
    }

    public T withInputs(InventoryItemSlot... itemSlots) {
        inputItemSlots.addAll(Arrays.asList(itemSlots));
        return (T) this;
    }

    public T withOutputs(InventoryItemSlot... itemSlots) {
        outputItemSlots.addAll(Arrays.asList(itemSlots));
        return (T) this;
    }

    public T withAuxiliaries(InventoryItemSlot... itemSlots) {
        auxilaryItemSlots.addAll(Arrays.asList(itemSlots));
        return (T) this;
    }

    public T withEnergyBuffer(EnergyBuffer buffer) {
        this.energyBuffer = buffer;
        return (T) this;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Getters

    public List<InventoryItemSlot> getInputItemSlots() {
        return inputItemSlots;
    }

    public List<InventoryItemSlot> getOutputItemSlots() {
        return outputItemSlots;
    }

    public List<InventoryItemSlot> getAuxilaryItemSlots() {
        return auxilaryItemSlots;
    }

    public EnergyBuffer getEnergyBuffer() {
        return energyBuffer;
    }

    // endregion
}
