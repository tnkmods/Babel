package com.thenatekirby.babel.core.inventory;

import com.thenatekirby.babel.core.energy.EnergyBuffer;
import com.thenatekirby.babel.core.slots.ItemSlot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class SlotConfiguration<T extends SlotConfiguration> {
    private List<ItemSlot> inputItemSlots = new ArrayList<>();
    private List<ItemSlot> outputItemSlots = new ArrayList<>();
    private List<ItemSlot> auxilaryItemSlots = new ArrayList<>();
    private EnergyBuffer energyBuffer = EnergyBuffer.ZERO;

    protected SlotConfiguration() {

    }

    public static <T extends SlotConfiguration> SlotConfiguration<T> make() {
        return new SlotConfiguration();
    }

    public T withInputs(ItemSlot... itemSlots) {
        inputItemSlots.addAll(Arrays.asList(itemSlots));
        return (T) this;
    }

    public T withOutputs(ItemSlot... itemSlots) {
        outputItemSlots.addAll(Arrays.asList(itemSlots));
        return (T) this;
    }

    public T withEnergyBuffer(EnergyBuffer buffer) {
        this.energyBuffer = buffer;
        return (T) this;
    }

    public T withAuxiliaries(ItemSlot... itemSlots) {
        auxilaryItemSlots.addAll(Arrays.asList(itemSlots));
        return (T) this;
    }

    public List<ItemSlot> getInputItemSlots() {
        return inputItemSlots;
    }

    public List<ItemSlot> getOutputItemSlots() {
        return outputItemSlots;
    }

    public List<ItemSlot> getAuxilaryItemSlots() {
        return auxilaryItemSlots;
    }

    public EnergyBuffer getEnergyBuffer() {
        return energyBuffer;
    }
}
