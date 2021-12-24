package com.thenatekirby.babel.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class ItemInWorldEntity extends ItemEntity {
    private int workTicks = 0;
    private boolean ticking = true;

    public ItemInWorldEntity(EntityType<? extends ItemEntity> entityType, Level level) {
        super(entityType, level);
    }

    // ====---------------------------------------------------------------------------====
    // region Ticking

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide || !ticking) {
            return;
        }

        if (workTicks == 20) {
            if (this.isInWater()) {
                onWaterTick();
            } else if (this.isInLava()) {
                onLavaTick();
            } else if (level.getBlockState(blockPosition()) == Blocks.FIRE.defaultBlockState()) {
                onFireTick();
            } else if (this.isOnGround()) {
                onGroundTick();
            }

            workTicks = 0;
        } else {
            workTicks++;
        }
    }

    public void onWaterTick() {
    }

    public void onLavaTick() {
    }

    public void onFireTick() {
    }

    public void onGroundTick() {
    }

    public void setComplete() {
        this.ticking = false;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Packet

    @Nonnull
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    // endregion
}
