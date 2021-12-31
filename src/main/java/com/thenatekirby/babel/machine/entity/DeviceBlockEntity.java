package com.thenatekirby.babel.machine.entity;

import com.thenatekirby.babel.core.NBTConstants;
import com.thenatekirby.babel.core.progress.Progress;
import com.thenatekirby.babel.machine.config.EnergyStats;
import com.thenatekirby.babel.machine.config.RedstoneMode;
import com.thenatekirby.babel.machine.config.WorkingArea;
import com.thenatekirby.babel.util.ServerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class DeviceBlockEntity extends TickingBlockEntity {
    private boolean working = false;

    @Nonnull
    protected Progress progress = Progress.make();

    @Nonnull
    protected List<ItemStack> results = new ArrayList<>();

    // Machine State
    private boolean isProcessingOutput = false;
    private RedstoneMode redstoneMode = RedstoneMode.make();
    protected EnergyStats energyStats = EnergyStats.make();

    // Work Info
    private int workDuration = 0;
    private float workEfficiency = 1.0f;
    private float workSpeed = 1.0f;
    private int baseConsumption = 0;

    // Working Area
    protected WorkingArea workingArea = new WorkingArea(0, 0,0, WorkingArea.WorkingDirection.AROUND);
    private boolean showingWorkingArea = false;

    public DeviceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    @Nonnull
    public Progress getProgress() {
        return progress;
    }

    @Nonnull
    public RedstoneMode getRedstoneMode() {
        return redstoneMode;
    }

    public EnergyStats getEnergyStats() {
        return energyStats;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Lifecycle

    @Override
    protected void workTick(@Nonnull ServerLevel level) {
        if (progress.canAdvance()) {
            if (progress.getProgressCurrent() == 0 && !this.onWorkStart(level)) {
                updateWorkState(level, false);
                return;
            }

            if (!results.isEmpty() && !canOutput(results)) {
                updateWorkState(level, false);
                return;
            }

            onUpdateEnergyStats(level);
            if (!shouldPerformWork(level)) {
                updateWorkState(level, false);
                return;
            }

            updateWorkState(level, true);
            if (progress.isComplete()) {
                isProcessingOutput = true;
                if (!onWorkComplete(level)) {
                    isProcessingOutput = false;
                    return;
                }

                consumeEnergy(level);
                onResetWork(level);

            } else {
                progress.advance();
                consumeEnergy(level);
            }
        } else {
            updateWorkState(level, false);
        }
    }

    public void onInventoryChanged(@Nonnull ServerLevel level) {
        if (!isProcessingOutput) {
            handleInventoryChange(level);
        }
    }

    private void handleInventoryChange(@Nonnull ServerLevel world) {
        onCalculateEfficiency(world);
        onCalculateResult(world);
        updateEfficiencyStats();
    }

    protected void onCalculateEfficiency(@Nonnull ServerLevel world) {
    }

    protected void onCalculateResult(@Nonnull ServerLevel world) {
    }

    protected void onUpdateEnergyStats(@Nonnull ServerLevel world) {
    }

    protected boolean onWorkStart(@Nonnull ServerLevel world) {
        return true;
    }

    protected boolean shouldPerformWork(@Nonnull ServerLevel world) {
        return canConsumeEnergy(world) && isEnabledByRedstone(world);
    }

    protected boolean canConsumeEnergy(@Nonnull ServerLevel world) {
        if (energyStats.getConsumes() > 0) {
            return inventory.getEnergyStorage().hasAtleast(energyStats.getConsumes());
        }

        return true;
    }

    protected void consumeEnergy(@Nonnull ServerLevel world) {
        inventory.getEnergyStorage().consumeEnergy(energyStats.getConsumes());
    }

    protected boolean onWorkComplete(@Nonnull ServerLevel world) {
        if (!results.isEmpty()) {
            for (ItemStack itemStack : results) {
                inventory.transferToOutput(itemStack);
            }
        }

        return true;
    }

    protected boolean isEnabledByRedstone(@Nonnull ServerLevel world) {
        if (this.redstoneMode.isIgnored()) {
            return true;
        }

        if (this.redstoneMode.whenPowered()) {
            return world.getDirectSignalTo(worldPosition) > 0;
        } else {
            return world.getDirectSignalTo(worldPosition) == 0;
        }
    }

    protected boolean isEnabledByRedstoneWeak(@Nonnull ServerLevel world) {
        if (this.redstoneMode.isIgnored()) {
            return true;
        }

        if (this.redstoneMode.whenPowered()) {
            return world.getBestNeighborSignal(worldPosition) > 0;
        } else {
            return world.getBestNeighborSignal(worldPosition) == 0;
        }
    }

    protected void onResetWork(@Nonnull ServerLevel world) {
        energyStats.setConsumes(0);
        progress.clear();
        results.clear();

        handleInventoryChange(world);
        isProcessingOutput = false;
    }

    protected void onWorkStateChanged(@Nonnull ServerLevel world, boolean workState) {
    }

    private void updateWorkState(@Nonnull ServerLevel world, boolean workState) {
        if (this.working != workState) {
            this.working = workState;
            if (!workState) {
                this.energyStats.setConsumes(0);
            }

            this.onWorkStateChanged(world, workState);
        }
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Stats

    public void setWorkDuration(int duration) {
        this.workDuration = duration;

        if (duration == 0) {
            progress.clear();
        }
    }

    public void setWorkEfficiency(float workEfficiency) {
        this.workEfficiency = workEfficiency;
        energyStats.setEfficiency(workEfficiency);
    }

    public void setWorkSpeed(float workSpeed) {
        this.workSpeed = workSpeed;
        energyStats.setSpeed(workSpeed);
    }

    protected float getProgressWorkSpeed() {
        return workSpeed;
    }

    public void setBaseEnergyConsumption(int energyConsumption) {
        if (baseConsumption != energyConsumption || energyStats.getConsumes() == 0) {
            this.baseConsumption = energyConsumption;
            updateEfficiencyStats();
        }
    }

    private void updateEfficiencyStats() {
        if (progress.canAdvance()) {
            int fePerTick = Math.round((baseConsumption * workSpeed) / workEfficiency);
            this.energyStats.setConsumes(fePerTick);
        } else {
            this.energyStats.setConsumes(0);
        }

        this.progress.setMax(Math.round(workDuration / getProgressWorkSpeed()));
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Helpers

    protected boolean canOutput(@Nonnull ItemStack itemStack) {
        return inventory.canOutput(itemStack);
    }

    protected boolean canOutput(@Nonnull List<ItemStack> itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            if (!canOutput(itemStack)) {
                return false;
            }
        }

        return true;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Working Areas

    protected WorkingArea.Modifier getWorkAreaRangeModifier() {
        return WorkingArea.Modifier.ZERO;
    }

    protected Direction getWorkingAreaFacingDirection() {
        return Direction.NORTH;
    }

    protected AABB getWorkingAreaAABB() {
        return workingArea.makeAABBFromPos(worldPosition, getWorkingAreaFacingDirection(), getWorkAreaRangeModifier());
    }

    public VoxelShape getWorkingAreaVoxelShape() {
        return workingArea.getVoxelShape(worldPosition, getWorkingAreaFacingDirection(), getWorkAreaRangeModifier());
    }

    public boolean isShowingWorkingArea() {
        return showingWorkingArea;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region NBT


    @Override
    protected void loadBlockEntity(@Nonnull CompoundTag compoundTag) {
        super.loadBlockEntity(compoundTag);
        progress.deserializeNBT(compoundTag.getCompound("progress"));
        redstoneMode.deserializeNBT(compoundTag.getCompound("redstone_mode"));

        if (compoundTag.contains("results", NBTConstants.TAG_LIST)) {
            results.clear();

            ListTag listNBT = compoundTag.getList("results", NBTConstants.TAG_COMPOUND);
            for (int idx = 0; idx < listNBT.size(); idx++) {
                results.add(ItemStack.of(listNBT.getCompound(idx)));
            }
        }

        showingWorkingArea = compoundTag.getBoolean("showing_working_area");
        ServerUtil.ifServer(level, this::onCalculateResult);
    }

    @Override
    protected void serializeBlockEntity(CompoundTag compoundTag) {
        compoundTag.put("progress", progress.serializeNBT());

        if (!results.isEmpty()) {
            ListTag listNBT = new ListTag();
            for (ItemStack itemStack : results) {
                listNBT.add(itemStack.serializeNBT());
            }

            compoundTag.put("results", listNBT);
        }

        compoundTag.put("redstone_mode", redstoneMode.serializeNBT());
        compoundTag.putBoolean("showing_working_area", showingWorkingArea);

        super.serializeBlockEntity(compoundTag);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Packets

    public void toggleRedstoneMode(boolean forward) {
        this.redstoneMode.toggle(forward);
        setChanged();
    }

    public void toggleShowingWorkingArea() {
        this.showingWorkingArea = !this.showingWorkingArea;
        setChanged();
    }
}
