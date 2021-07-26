package com.thenatekirby.babel.core.container;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.api.IProgress;
import com.thenatekirby.babel.api.ISyncable;
import com.thenatekirby.babel.core.BabelConstants;
import com.thenatekirby.babel.core.inventory.ContainerInventory;
import com.thenatekirby.babel.core.slots.BabelSlot;
import com.thenatekirby.babel.core.slots.ItemSlot;
import com.thenatekirby.babel.core.tileentity.BabelTileEntity;
import com.thenatekirby.babel.mixin.ContainerMixin;
import com.thenatekirby.babel.network.packet.ContainerUpdateGuiPacket;
import com.thenatekirby.babel.network.sync.SyncableProgress;
import com.thenatekirby.babel.registration.DeferredContainer;
import com.thenatekirby.babel.util.ServerUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BabelContainer extends Container {
    private ContainerConfig containerConfig;
    private IItemHandler playerInventory;
    private World world;

    private ContainerInventory containerInventory;
    private BabelTileEntity tileEntity;
    private List<ISyncable> syncables;
    private PlayerEntity playerEntity;

    private SyncableProgress powerProgress;
    protected BlockPos blockPos;

    protected BabelContainer(@Nonnull ContainerConfig containerConfig, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(containerConfig.getContainerType(), windowId);

        this.containerConfig = containerConfig;
        this.playerInventory = new InvWrapper(playerInventory);
        this.world = world;
        this.playerEntity = player;
        this.blockPos = pos;

        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof BabelTileEntity) {
            this.tileEntity = (BabelTileEntity) tileEntity;
            this.containerInventory = ((BabelTileEntity) tileEntity).getInventory();
        }

        initContainer();
        onPostInit();
    }

    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    public BabelTileEntity getTileEntity() {
        return tileEntity;
    }

    public ContainerInventory getContainerInventory() {
        return containerInventory;
    }

    protected List<IContainerListener> getContainerListeners() {
        return ((ContainerMixin) this).getListeners();
    }

    protected List<ISyncable> getSyncables() {
        List<ISyncable> syncables = new ArrayList<>();
        if (containerInventory != null && containerInventory.getEnergyStorage().getCapacity() > 0) {
            powerProgress = SyncableProgress.from(containerInventory.getEnergyStorage());
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
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return true;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Networking

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        ServerUtil.ifServer(tileEntity, world -> {
            PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());

            for (ISyncable syncable : syncables) {
                syncable.write(packetBuffer);
            }

            ContainerUpdateGuiPacket packet = new ContainerUpdateGuiPacket(windowId, packetBuffer);
            List<IContainerListener> listeners = getContainerListeners();
            for (IContainerListener listener : listeners) {
                if (listener instanceof ServerPlayerEntity) {
                    Babel.NETWORK.sendToPlayer((ServerPlayerEntity) listener, packet);
                }
            }
        });
    }

    public void receive(ContainerUpdateGuiPacket packet) {
        PacketBuffer packetBuffer = packet.getBuffer();

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
        if (this.tileEntity != null) {
            setupSlots();
            setupSyncedData();
        }

        onContainerOpen(playerEntity);
    }

    protected void onContainerOpen(PlayerEntity playerEntity) {
    }

    private void setupSlots() {
        layoutMachineSlots();
        layoutPlayerSlots();
    }

    private void setupSyncedData() {
        this.syncables = getSyncables();
    }

    private void layoutMachineSlots() {
        ContainerInventory inventory = getContainerInventory();
        if (tileEntity == null || inventory == null) {
            return;
        }

        for (ItemSlot itemSlot : inventory.getAllItemHandler().getAllSlots()) {
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
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack outputItemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        ContainerInventory containerInventory = getContainerInventory();

        if (containerInventory == null) {
            return ItemStack.EMPTY;
        }

        if (slot != null && slot.getHasStack()) {
            ItemStack inputItemStack = slot.getStack();
            outputItemStack = inputItemStack.copy();

            int machineSlotCount = containerInventory.getSlotCount();
            int inventorySlotEnd = machineSlotCount + BabelConstants.PLAYER_INV_SLOT_COUNT;

            if (index < machineSlotCount) {
                if (!this.mergeItemStack(inputItemStack, machineSlotCount, inventorySlotEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(inputItemStack, inputItemStack);

            } else {
                if (containerInventory.canInsert(inputItemStack)) {
                    if (!this.mergeItemStack(inputItemStack, 0, machineSlotCount, false)) {
                        return ItemStack.EMPTY;
                    }

                } else if (index < machineSlotCount + 27) {
                    // Player Inventory (non-hotbar) to hotbar

                    if (!this.mergeItemStack(inputItemStack, machineSlotCount + 27, inventorySlotEnd, false)) {
                        return ItemStack.EMPTY;
                    }

                } else if (index < inventorySlotEnd) {
                    // Player hotbar to inventory

                    if (!this.mergeItemStack(inputItemStack, machineSlotCount, machineSlotCount + 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (inputItemStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
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

    public static class ContainerConfig {
        @Nullable
        private ContainerType<?> containerType;

        private int playerInventoryStartX;
        private int playerInventoryStartY;

        private ContainerConfig(@Nullable ContainerType<?> containerType, int playerInventoryStartX, int playerInventoryStartY) {
            this.containerType = containerType;
            this.playerInventoryStartX = playerInventoryStartX;
            this.playerInventoryStartY = playerInventoryStartY;
        }

        public static ContainerConfig EMPTY = new ContainerConfig(null, 8, 84);

        public static ContainerConfig from(DeferredContainer<?> deferredContainer) {
            return new ContainerConfig(deferredContainer.getContainerType(), 8, 84);
        }

        public static ContainerConfig from(DeferredContainer<?> deferredContainer, int startX, int startY) {

            return new ContainerConfig(deferredContainer.getContainerType(), startX, startY);
        }

        @Nullable
        ContainerType<?> getContainerType() {
            return containerType;
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
