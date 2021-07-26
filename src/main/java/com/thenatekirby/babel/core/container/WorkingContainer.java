package com.thenatekirby.babel.core.container;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.api.IBooleanProvider;
import com.thenatekirby.babel.api.ISyncable;
import com.thenatekirby.babel.core.tileentity.WorkingTileEntity;
import com.thenatekirby.babel.gui.button.GuiButton;
import com.thenatekirby.babel.gui.button.GuiRedstoneToggleView;
import com.thenatekirby.babel.network.packet.ToggleRedstoneModePacket;
import com.thenatekirby.babel.network.sync.SyncableEnergyStats;
import com.thenatekirby.babel.network.sync.SyncableProgress;
import com.thenatekirby.babel.network.sync.SyncableRedstoneMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

import com.thenatekirby.babel.core.container.BabelContainer.ContainerConfig;

public class WorkingContainer extends BabelContainer {
    private SyncableProgress progress;
    private SyncableRedstoneMode redstoneMode;
    private SyncableEnergyStats energyStats;

    protected WorkingContainer(@Nonnull ContainerConfig containerConfig, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(containerConfig, windowId, world, pos, playerInventory, player);
    }

    // ====---------------------------------------------------------------------------====
    // region Lifecycle

    @Override
    protected List<ISyncable> getSyncables() {
        List<ISyncable> syncables = super.getSyncables();

        this.progress = SyncableProgress.from(getWorkingTileEntity().getProgress());
        syncables.add(this.progress);

        this.redstoneMode = SyncableRedstoneMode.from(getWorkingTileEntity().getRedstoneMode());
        syncables.add(this.redstoneMode);

        this.energyStats = SyncableEnergyStats.from(getWorkingTileEntity().getEnergyStats());
        syncables.add(this.energyStats);

        return syncables;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    protected WorkingTileEntity getWorkingTileEntity() {
        return (WorkingTileEntity) this.getTileEntity();
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
        return () -> getWorkingTileEntity().isShowingWorkingArea();
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Helpers

    public GuiRedstoneToggleView.IOnRedstoneToggledListener makeRedstoneToggledListener() {
        return (forward) -> Babel.NETWORK.sendToServer(new ToggleRedstoneModePacket(blockPos, forward));
    }

    public GuiButton.IOnClickListener makeWorkingAreaToggledListener() {
        return (context) -> getWorkingTileEntity().toggleShowingWorkingArea();
    }
}
