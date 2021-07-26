package com.thenatekirby.babel.core.tileentity;

import com.thenatekirby.babel.core.Progress;
import com.thenatekirby.babel.core.RedstoneMode;
import com.thenatekirby.babel.core.WorkingArea;
import com.thenatekirby.babel.core.energy.EnergyStats;
import com.thenatekirby.babel.util.ServerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class WorkingTileEntity extends BabelTileEntity implements ITickableTileEntity {
    private boolean working = false;

    @Nonnull
    protected Progress progress = Progress.make();

    @Nonnull
    protected List<ItemStack> results = new ArrayList<>();

    // Machine State
    private boolean isProcessingOutput = false;
    private RedstoneMode redstoneMode = RedstoneMode.make();
    protected EnergyStats energyStats = new EnergyStats(0, 0);

    // Work Info
    private int workDuration = 0;
    private float workEfficiency = 1.0f;
    private float workSpeed = 1.0f;
    private int baseConsumption = 0;

    // Working Area
    protected WorkingArea workingArea = new WorkingArea(0, 0,0, WorkingArea.WorkingDirection.AROUND);
    private boolean showingWorkingArea = false;

    public WorkingTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
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
    // region ITickableTileEntity

    @Override
    public void tick() {
        if (world == null || world.isRemote) {
            return;
        }

        workTick((ServerWorld) world);
    }

    protected void workTick(@Nonnull ServerWorld world) {
        if (progress.canAdvance()) {
            if (progress.getProgressCurrent() == 0 && !this.onWorkStart(world)) {
                updateWorkState(world, false);
                return;
            }

            if (!results.isEmpty() && !canOutput(results)) {
                updateWorkState(world, false);
                return;
            }

            onUpdateEnergyStats(world);
            if (!shouldPerformWork(world)) {
                updateWorkState(world, false);
                return;
            }

            updateWorkState(world, true);
            if (progress.isComplete()) {
                isProcessingOutput = true;
                if (!onWorkComplete(world)) {
                    isProcessingOutput = false;
                    return;
                }

                consumeEnergy(world);
                onResetWork(world);

            } else {
                progress.advance();
                consumeEnergy(world);
            }
        } else {
            updateWorkState(world, false);
        }
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Lifecycle

    @Override
    public void setWorldAndPos(@Nonnull World world, BlockPos pos) {
        super.setWorldAndPos(world, pos);

        if (!world.isRemote) {
            this.onInventoryChanged((ServerWorld) world);
        }
    }

    @Override
    public void onInventoryChanged(@Nonnull ServerWorld world) {
        super.onInventoryChanged(world);

        if (!isProcessingOutput) {
            handleInventoryChange(world);
        }
    }

    private void handleInventoryChange(@Nonnull ServerWorld world) {
        onCalculateEfficiency(world);
        onCalculateResult(world);
        updateEfficiencyStats();
    }

    protected void onCalculateEfficiency(@Nonnull ServerWorld world) {
    }

    protected void onCalculateResult(@Nonnull ServerWorld world) {
    }

    protected void onUpdateEnergyStats(@Nonnull ServerWorld world) {
    }

    protected boolean onWorkStart(@Nonnull ServerWorld world) {
        return true;
    }

    protected boolean shouldPerformWork(@Nonnull ServerWorld world) {
        return canConsumeEnergy(world) && isEnabledByRedstone(world);
    }

    protected boolean canConsumeEnergy(@Nonnull ServerWorld world) {
        if (energyStats.getConsumes() > 0) {
            return inventory.getEnergyStorage().hasAtleast(energyStats.getConsumes());
        }

        return true;
    }

    protected void consumeEnergy(@Nonnull ServerWorld world) {
        inventory.getEnergyStorage().consumeEnergy(energyStats.getConsumes());
    }

    protected boolean onWorkComplete(@Nonnull ServerWorld world) {
        if (!results.isEmpty()) {
            for (ItemStack itemStack : results) {
                inventory.transferToOutput(itemStack);
            }
        }

        return true;
    }

    protected boolean isEnabledByRedstone(@Nonnull ServerWorld world) {
        if (this.redstoneMode.isIgnored()) {
            return true;
        }

        if (this.redstoneMode.whenPowered()) {
            return world.getStrongPower(pos) > 0;
        } else {
            return world.getStrongPower(pos) == 0;
        }
    }

    protected boolean isEnabledByRedstoneWeak(@Nonnull ServerWorld world) {
        if (this.redstoneMode.isIgnored()) {
            return true;
        }

        if (this.redstoneMode.whenPowered()) {
            return world.getRedstonePowerFromNeighbors(pos) > 0;
        } else {
            return world.getRedstonePowerFromNeighbors(pos) == 0;
        }
    }

    protected void onResetWork(@Nonnull ServerWorld world) {
        energyStats.setConsumes(0);
        progress.clear();
        results.clear();

        handleInventoryChange(world);
        isProcessingOutput = false;
    }

    protected void onWorkStateChanged(@Nonnull ServerWorld world, boolean workState) {
    }

    private void updateWorkState(@Nonnull ServerWorld world, boolean workState) {
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

    protected AxisAlignedBB getWorkingAreaAABB() {
        return workingArea.makeAABBFromPos(pos, getWorkingAreaFacingDirection(), getWorkAreaRangeModifier());
    }

    public VoxelShape getWorkingAreaVoxelShape() {
        return workingArea.getVoxelShape(pos, getWorkingAreaFacingDirection(), getWorkAreaRangeModifier());
    }

    public boolean isShowingWorkingArea() {
        return showingWorkingArea;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region NBT

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        progress.deserializeNBT(nbt.getCompound("progress"));
        redstoneMode.deserializeNBT(nbt.getCompound("redstone_mode"));

        if (nbt.contains("results", Constants.NBT.TAG_LIST)) {
            results.clear();

            ListNBT listNBT = nbt.getList("results", Constants.NBT.TAG_COMPOUND);
            for (int idx = 0; idx < listNBT.size(); idx++) {
                results.add(ItemStack.read(listNBT.getCompound(idx)));
            }
        }

        showingWorkingArea = nbt.getBoolean("showing_working_area");
        ServerUtil.ifServer(world, this::onCalculateResult);
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("progress", progress.serializeNBT());

        if (!results.isEmpty()) {
            ListNBT listNBT = new ListNBT();
            for (ItemStack itemStack : results) {
                listNBT.add(itemStack.serializeNBT());
            }

            compound.put("results", listNBT);
        }

        compound.put("redstone_mode", redstoneMode.serializeNBT());
        compound.putBoolean("showing_working_area", showingWorkingArea);

        return super.write(compound);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Packets

    public void toggleRedstoneMode(boolean forward) {
        this.redstoneMode.toggle(forward);
        markDirty();
    }

    public void toggleShowingWorkingArea() {
        this.showingWorkingArea = !this.showingWorkingArea;
        markDirty();
    }
}