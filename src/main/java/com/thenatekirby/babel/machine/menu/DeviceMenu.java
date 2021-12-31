package com.thenatekirby.babel.machine.menu;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.capability.item.ValidatedSlotItemHandler;
import com.thenatekirby.babel.core.api.IBooleanProvider;
import com.thenatekirby.babel.core.api.ISyncable;
import com.thenatekirby.babel.gui.buttons.GuiButton;
import com.thenatekirby.babel.gui.buttons.GuiRedstoneToggleButton;
import com.thenatekirby.babel.machine.config.InventoryItemSlot;
import com.thenatekirby.babel.machine.entity.DeviceBlockEntity;
import com.thenatekirby.babel.machine.inventory.DeviceInventory;
import com.thenatekirby.babel.machine.slot.BabelSlot;
import com.thenatekirby.babel.network.packet.ToggleRedstoneModePacket;
import com.thenatekirby.babel.network.sync.SyncableEnergyStats;
import com.thenatekirby.babel.network.sync.SyncableProgress;
import com.thenatekirby.babel.network.sync.SyncableRedstoneMode;
import com.thenatekirby.babel.util.BabelConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class DeviceMenu<T extends DeviceBlockEntity> extends BabelMenu {
    private DeviceInventory<T> deviceInventory;
    private T blockEntity;

    private SyncableProgress powerProgress;
    private SyncableProgress progress;
    private SyncableRedstoneMode redstoneMode;
    private SyncableEnergyStats energyStats;

    protected DeviceMenu(@Nonnull MenuConfig containerConfig, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        super(containerConfig, windowId, world, pos, playerInventory, player);
    }

    // ====---------------------------------------------------------------------------====
    // region Lifecycle

    @SuppressWarnings("unchecked")
    @Override
    protected void onInitMenu(@Nonnull Level level, @Nonnull BlockPos blockPos) {
        super.onInitMenu(level, blockPos);

        T blockEntity = (T) level.getBlockEntity(blockPos);
        if (blockEntity != null) {
            this.blockEntity = blockEntity;
            this.deviceInventory = blockEntity.getInventory();
        }
    }

    @Override
    protected void onPostInit() {
        super.onPostInit();

        if (this.blockEntity != null) {
            setupPlayerSlots();
            setupMachineSlots();
        }
    }

    protected void setupMachineSlots() {
        DeviceInventory<T> inventory = getDeviceInventory();
        if (blockEntity == null || inventory == null) {
            return;
        }

        for (InventoryItemSlot itemSlot : inventory.getAllItemHandler().getAllSlots()) {
            addSlot(new BabelSlot(itemSlot));
        }
    }

    @Override
    protected List<ISyncable> getSyncables() {
        List<ISyncable> syncables = super.getSyncables();

        if (deviceInventory != null && deviceInventory.getEnergyStorage().getCapacity() > 0) {
            powerProgress = SyncableProgress.from(deviceInventory.getEnergyStorage());
            syncables.add(powerProgress);
        }

        this.progress = SyncableProgress.from(getBlockEntity().getProgress());
        syncables.add(this.progress);

        this.redstoneMode = SyncableRedstoneMode.from(getBlockEntity().getRedstoneMode());
        syncables.add(this.redstoneMode);

        this.energyStats = SyncableEnergyStats.from(getBlockEntity().getEnergyStats());
        syncables.add(this.energyStats);

        return syncables;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    protected T getBlockEntity() {
        return blockEntity;
    }

    protected DeviceInventory<T> getDeviceInventory() {
        return deviceInventory;
    }

    public SyncableProgress getProgress() {
        return progress;
    }

    public SyncableRedstoneMode getRedstoneMode() {
        return redstoneMode;
    }

    public SyncableEnergyStats getEnergyStats() {
        return energyStats;
    }

    public IBooleanProvider getWorkingAreaVisible() {
        return () -> getBlockEntity().isShowingWorkingArea();
    }

    public SyncableProgress getPowerProgress() {
        return powerProgress;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Helpers

    public GuiRedstoneToggleButton.IOnRedstoneToggledListener makeRedstoneToggledListener() {
        return (forward) -> Babel.NETWORK.sendToServer(new ToggleRedstoneModePacket(getBlockPos(), forward));
    }

    public GuiButton.IOnClickListener makeWorkingAreaToggledListener() {
        return (context) -> getBlockEntity().toggleShowingWorkingArea();
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Item Handling

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player playerIn, int index) {
        ItemStack outputItemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        DeviceInventory<T> machineInventory = getDeviceInventory();

        if (machineInventory == null) {
            return ItemStack.EMPTY;
        }

        if (slot.hasItem()) {
            ItemStack inputItemStack = slot.getItem();
            outputItemStack = inputItemStack.copy();

            int machineSlotCount = machineInventory.getSlotCount();
            int inventorySlotEnd = machineSlotCount + BabelConstants.PLAYER_INV_SLOT_COUNT;

            if (index < machineSlotCount) {
                if (!this.moveItemStackTo(inputItemStack, machineSlotCount, inventorySlotEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(inputItemStack, inputItemStack);

            } else {
                if (machineInventory.canInsert(inputItemStack)) {
                    if (!this.moveItemStackTo(inputItemStack, 0, machineSlotCount, false)) {
                        return ItemStack.EMPTY;
                    }

                } else if (index < machineSlotCount + 27) {
                    // Player Inventory (non-hotbar) to hotbar

                    if (!this.moveItemStackTo(inputItemStack, machineSlotCount + 27, inventorySlotEnd, false)) {
                        return ItemStack.EMPTY;
                    }

                } else if (index < inventorySlotEnd) {
                    // Player hotbar to inventory

                    if (!this.moveItemStackTo(inputItemStack, machineSlotCount, machineSlotCount + 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (inputItemStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (inputItemStack.getCount() == outputItemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, inputItemStack);
        }

        return outputItemStack;
    }

    // endregion
}
