package com.thenatekirby.babel.core.inventory;

import com.thenatekirby.babel.core.energy.EnergyBuffer;
import com.thenatekirby.babel.core.slots.ItemSlot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlotConfiguration {
    private List<ItemSlot> inputItemSlots = new ArrayList<>();
    private List<ItemSlot> outputItemSlots = new ArrayList<>();
    private List<ItemSlot> auxilaryItemSlots = new ArrayList<>();
    private EnergyBuffer energyBuffer = EnergyBuffer.ZERO;

    private SlotConfiguration() {

    }

    public static SlotConfiguration make() {
        return new SlotConfiguration();
    }

    public SlotConfiguration withInputs(ItemSlot... itemSlots) {
        inputItemSlots.addAll(Arrays.asList(itemSlots));
        return this;
    }

    public SlotConfiguration withOutputs(ItemSlot... itemSlots) {
        outputItemSlots.addAll(Arrays.asList(itemSlots));
        return this;
    }

    public SlotConfiguration withEnergyBuffer(EnergyBuffer buffer) {
        this.energyBuffer = buffer;
        return this;
    }

    public SlotConfiguration withAuxiliaries(ItemSlot... itemSlots) {
        auxilaryItemSlots.addAll(Arrays.asList(itemSlots));
        return this;
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
