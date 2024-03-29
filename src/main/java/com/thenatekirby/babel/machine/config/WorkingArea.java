package com.thenatekirby.babel.machine.config;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class WorkingArea {
    public enum WorkingDirection {
        AROUND,
        FRONT
    }

    public static class Modifier {
        int x;
        int y;
        int z;

        public Modifier(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Modifier(int range) {
            this.x = range;
            this.y = range;
            this.z = range;
        }

        public static final Modifier ZERO = new Modifier(0, 0, 0);
    }

    private final int x;
    private final int y;
    private final int z;
    private final WorkingDirection direction;

    public WorkingArea(int x, int y, int z, WorkingDirection direction) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.direction = direction;
    }

    public static final WorkingArea NONE = new WorkingArea(0, 0,0, WorkingDirection.AROUND);


    public AABB makeAABBFromPos(@Nonnull BlockPos blockPos, Direction facing, Modifier modifier) {
        int x = this.x + modifier.x;
        int y = this.y + modifier.y;
        int z = this.z + modifier.z;

        BlockPos originPos = blockPos;
        if (this.direction == WorkingDirection.FRONT) {
            originPos = getOffsetPos(facing, originPos, x + 1, y + 1, z + 1);
        }

        return new AABB(originPos).inflate(x, y, z);
    }

    private BlockPos getOffsetPos(Direction facing, BlockPos originalPos, int x, int y, int z) {
        switch (facing) {
            case NORTH: {
                return originalPos.relative(Direction.NORTH, x);
            }
            case SOUTH: {
                return originalPos.relative(Direction.SOUTH, x);
            }
            case EAST: {
                return originalPos.relative(Direction.EAST, z);
            }
            case WEST: {
                return originalPos.relative(Direction.WEST, z);
            }
            default: {
                return originalPos;
            }
        }
    }

    public VoxelShape getVoxelShape(@Nonnull BlockPos blockPos, Direction facing, Modifier modifier) {
        return Shapes.create(makeAABBFromPos(blockPos, facing, modifier));
    }
}
