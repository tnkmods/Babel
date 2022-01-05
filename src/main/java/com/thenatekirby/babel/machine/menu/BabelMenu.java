package com.thenatekirby.babel.machine.menu;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.core.api.ISyncable;
import com.thenatekirby.babel.network.packet.ContainerUpdateGuiPacket;
import com.thenatekirby.babel.registration.DeferredMenu;
import com.thenatekirby.babel.util.ServerUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// ====---------------------------------------------------------------------------====

@SuppressWarnings("rawtypes")
public abstract class BabelMenu extends AbstractContainerMenu {
    private final MenuConfig containerConfig;
    private final IItemHandler playerInventory;

    private List<ISyncable> syncables;
    private final Player player;
    private final UUID playerUUID;

    private final BlockPos blockPos;
    private final Level level;

    protected BabelMenu(@Nonnull MenuConfig containerConfig, int windowId, Level level, BlockPos pos, Inventory playerInventory, Player player) {
        super(containerConfig.getMenuType(), windowId);

        this.containerConfig = containerConfig;
        this.playerInventory = new InvWrapper(playerInventory);
        this.player = player;
        this.playerUUID = player.getUUID();
        this.blockPos = pos;
        this.level = level;

        onInitMenu(level, blockPos);
        onPostInit();
        onMenuOpen(player);
    }

    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    protected List<ContainerListener> getContainerListeners() {
        return containerListeners;
    }

    protected List<ISyncable> getSyncables() {
        return new ArrayList<>();
    }

    @Nonnull
    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Player getPlayer() {
        return player;
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

        ServerUtil.ifServer(level, serverLevel -> {
            FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());

            for (ISyncable syncable : syncables) {
                syncable.write(packetBuffer);
            }

            // TODO: Is this the correct way of doing this?
            Player serverPlayer = serverLevel.getPlayerByUUID(playerUUID);
            if (serverPlayer instanceof ServerPlayer) {
            ContainerUpdateGuiPacket packet = new ContainerUpdateGuiPacket(containerId, packetBuffer);
//            List<ContainerListener> listeners = getContainerListeners();
//            for (ContainerListener listener : listeners) {
//                if (listener instanceof ServerPlayer) {
                    Babel.NETWORK.sendToPlayer((ServerPlayer) serverPlayer, packet);
//                }
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

    protected void onInitMenu(@Nonnull Level level, @Nonnull BlockPos blockPos) {
    }

    protected void onPostInit() {
        this.syncables = getSyncables();
    }

    protected void onMenuOpen(Player playerEntity) {
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Player Inventory

    public void setupPlayerSlots() {
        layoutPlayerInventorySlots(containerConfig.getPlayerInventoryStartX(), containerConfig.getPlayerInventoryStartY());
    }

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
