package com.thenatekirby.babel.machine.menu;

import com.thenatekirby.babel.core.api.IBooleanProvider;
import com.thenatekirby.babel.core.api.ISyncable;
import com.thenatekirby.babel.machine.entity.WorkingBlockEntity;
import com.thenatekirby.babel.network.sync.SyncableEnergyStats;
import com.thenatekirby.babel.network.sync.SyncableProgress;
import com.thenatekirby.babel.network.sync.SyncableRedstoneMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class WorkingMenu extends BabelMenu {
    private SyncableProgress progress;
    private SyncableRedstoneMode redstoneMode;
    private SyncableEnergyStats energyStats;

    protected WorkingMenu(@Nonnull MenuConfig containerConfig, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        super(containerConfig, windowId, world, pos, playerInventory, player);
    }

    // ====---------------------------------------------------------------------------====
    // region Lifecycle

    @Override
    protected List<ISyncable> getSyncables() {
        List<ISyncable> syncables = super.getSyncables();

        this.progress = SyncableProgress.from(getWorkingBlockEntity().getProgress());
        syncables.add(this.progress);

        this.redstoneMode = SyncableRedstoneMode.from(getWorkingBlockEntity().getRedstoneMode());
        syncables.add(this.redstoneMode);

        this.energyStats = SyncableEnergyStats.from(getWorkingBlockEntity().getEnergyStats());
        syncables.add(this.energyStats);

        return syncables;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    protected WorkingBlockEntity getWorkingBlockEntity() {
        return (WorkingBlockEntity) this.getBlockEntity();
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
        return () -> getWorkingBlockEntity().isShowingWorkingArea();
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Helpers

    // TODO: Gui
//    public GuiRedstoneToggleView.IOnRedstoneToggledListener makeRedstoneToggledListener() {
//        return (forward) -> Babel.NETWORK.sendToServer(new ToggleRedstoneModePacket(blockPos, forward));
//    }
//
//    public GuiButton.IOnClickListener makeWorkingAreaToggledListener() {
//        return (context) -> getWorkingBlockEntity().toggleShowingWorkingArea();
//    }
}
