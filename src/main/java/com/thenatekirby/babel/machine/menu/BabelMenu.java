package com.thenatekirby.babel.machine.menu;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.capability.item.BabelSlotItemHandler;
import com.thenatekirby.babel.core.api.ISyncable;
import com.thenatekirby.babel.machine.entity.BabelBlockEntity;
import com.thenatekirby.babel.machine.inventory.MachineInventory;
import com.thenatekirby.babel.machine.slot.BabelSlot;
import com.thenatekirby.babel.mixin.AbstractContainerMenuMixin;
import com.thenatekirby.babel.network.packet.ContainerUpdateGuiPacket;
import com.thenatekirby.babel.network.sync.SyncableProgress;
import com.thenatekirby.babel.registration.DeferredMenu;
import com.thenatekirby.babel.util.BabelConstants;
import com.thenatekirby.babel.util.ServerUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

// ====---------------------------------------------------------------------------====

@SuppressWarnings("rawtypes")
public class BabelMenu extends AbstractContainerMenu {
    private MenuConfig containerConfig;
    private IItemHandler playerInventory;
    private Level world;

    private MachineInventory machineInventory;
    private BabelBlockEntity blockEntity;
    private List<ISyncable> syncables;
    private Player playerEntity;

    private SyncableProgress powerProgress;
    protected BlockPos blockPos;

    protected BabelMenu(@Nonnull MenuConfig containerConfig, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        super(containerConfig.getMenuType(), windowId);

        this.containerConfig = containerConfig;
        this.playerInventory = new InvWrapper(playerInventory);
        this.world = world;
        this.playerEntity = player;
        this.blockPos = pos;

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof BabelBlockEntity) {
            this.blockEntity = (BabelBlockEntity) blockEntity;
            this.machineInventory = ((BabelBlockEntity) blockEntity).getInventory();
        }

        initContainer();
        onPostInit();
    }

    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    public BabelBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public MachineInventory getMachineInventory() {
        return machineInventory;
    }

    protected List<ContainerListener> getContainerListeners() {
        return ((AbstractContainerMenuMixin) this).getContainerListeners();
    }

    protected List<ISyncable> getSyncables() {
        List<ISyncable> syncables = new ArrayList<>();
        if (machineInventory != null && machineInventory.getEnergyStorage().getCapacity() > 0) {
            powerProgress = SyncableProgress.from(machineInventory.getEnergyStorage());
            syncables.add(powerProgress);
        }

        return syncables;
    }

    public SyncableProgress getPowerProgress() {
        return powerProgress;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Interaction

    @Override
    public boolean stillValid(@Nonnull Player playerIn) {
        return true;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Networking

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        ServerUtil.ifServer(blockEntity, world -> {
            FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());

            for (ISyncable syncable : syncables) {
                syncable.write(packetBuffer);
            }

            ContainerUpdateGuiPacket packet = new ContainerUpdateGuiPacket(containerId, packetBuffer);
            List<ContainerListener> listeners = getContainerListeners();
            for (ContainerListener listener : listeners) {
                if (listener instanceof ServerPlayer) {
                    Babel.NETWORK.sendToPlayer((ServerPlayer) listener, packet);
                }
            }
        });
    }

    public void receive(ContainerUpdateGuiPacket packet) {
        FriendlyByteBuf packetBuffer = packet.getBuffer();

        for (ISyncable syncable : syncables) {
            syncable.read(packetBuffer);
        }
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Lifecycle

    protected void initContainer() {
    }

    protected void onPostInit() {
        if (this.blockEntity != null) {
            setupSlots();
            setupSyncedData();
        }

        onContainerOpen(playerEntity);
    }

    protected void onContainerOpen(Player playerEntity) {
    }

    private void setupSlots() {
        layoutMachineSlots();
        layoutPlayerSlots();
    }

    private void setupSyncedData() {
        this.syncables = getSyncables();
    }

    private void layoutMachineSlots() {
        MachineInventory inventory = getMachineInventory();
        if (blockEntity == null || inventory == null) {
            return;
        }

        for (BabelSlotItemHandler itemSlot : inventory.getAllItemHandler().getAllSlots()) {
            addSlot(new BabelSlot(itemSlot));
        }
    }

    private void layoutPlayerSlots() {
        layoutPlayerInventorySlots(containerConfig.getPlayerInventoryStartX(), containerConfig.getPlayerInventoryStartY());
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Player Inventory

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    @SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }

        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Transferring

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player playerIn, int index) {
        ItemStack outputItemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        MachineInventory machineInventory = getMachineInventory();

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
    // ====---------------------------------------------------------------------------====
    // region Config

    public static class MenuConfig {
        @Nullable
        private final MenuType<?> menuType;

        private final int playerInventoryStartX;
        private final int playerInventoryStartY;

        private MenuConfig(@Nullable MenuType<?> containerType, int playerInventoryStartX, int playerInventoryStartY) {
            this.menuType = containerType;
            this.playerInventoryStartX = playerInventoryStartX;
            this.playerInventoryStartY = playerInventoryStartY;
        }

        public static MenuConfig EMPTY = new MenuConfig(null, 8, 84);

        public static MenuConfig from(DeferredMenu<?> deferredMenu) {
            return new MenuConfig(deferredMenu.getMenuType(), 8, 84);
        }

        public static MenuConfig from(DeferredMenu<?> deferredMenu, int startX, int startY) {
            return new MenuConfig(deferredMenu.getMenuType(), startX, startY);
        }

        @Nullable
        MenuType<?> getMenuType() {
            return menuType;
        }

        public int getPlayerInventoryStartX() {
            return playerInventoryStartX;
        }

        public int getPlayerInventoryStartY() {
            return playerInventoryStartY;
        }
    }

    // endregion
}
