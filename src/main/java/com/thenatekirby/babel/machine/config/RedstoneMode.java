package com.thenatekirby.babel.machine.config;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

// ====---------------------------------------------------------------------------====

public class RedstoneMode {
    private int state = 0;

    private RedstoneMode(int state) {
        this.state = state;
    }

    public static RedstoneMode make() {
        return new RedstoneMode(0);
    }

    // ====---------------------------------------------------------------------------====
    // region Helpers

    public boolean isIgnored() {
        return state == 0;
    }

    public boolean whenPowered() {
        return state == 1;
    }

    public boolean whenNotPowered() {
        return state == 2;
    }

    private void clampState() {
        this.state = Math.min(Math.max(this.state, 0), 2);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region NBT

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("state", state);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.state = nbt.getInt("state");
        clampState();
    }

    // ====---------------------------------------------------------------------------====
    // region Packet Buffer

    public void write(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(this.state);
    }

    public void read(FriendlyByteBuf packetBuffer) {
        this.state = packetBuffer.readInt();
        this.clampState();
    }

    public void toggle(boolean forward) {
        if (forward) {
            if (this.state == 2) {
                this.state = 0;
            } else {
                this.state += 1;
            }
        } else {
            if (this.state == 0) {
                this.state = 2;
            } else {
                this.state -= 1;
            }
        }
    }
}
