package com.thenatekirby.babel.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("WeakerAccess")
public class InWorldItemEntity extends ItemEntity {
    private int workTicks = 0;
    private boolean ticking = true;

    public InWorldItemEntity(EntityType<? extends ItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public InWorldItemEntity(EntityType<? extends ItemEntity> entityType, final World world, final double x,
                               final double y, final double z, final ItemStack stack) {
        this(entityType, world);
        this.setPosition(x, y, z);
        this.setItem(stack);

        this.rotationYaw = this.rand.nextFloat() * 360.0F;
        this.setMotion(this.rand.nextDouble() * 0.2D - 0.1D, 0.2D, this.rand.nextDouble() * 0.2D - 0.1D);
        this.lifespan = stack.getEntityLifespan(world);
    }

    @Override
    public void tick() {
        super.tick();

        if (world.isRemote || !ticking) {
            return;
        }

        if (workTicks == 20) {
            if (this.isInWater()) {
                onWaterTick();
            } else if (this.isInLava()) {
                onLavaTick();
            } else if (world.getBlockState(getPosition()) == Blocks.FIRE.getDefaultState()) {
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

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void setComplete() {
        this.ticking = false;
    }
}
