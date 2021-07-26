package com.thenatekirby.babel.core.capability;

import com.thenatekirby.babel.api.IExperienceStorage;
import com.thenatekirby.babel.api.IProgress;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class ExperienceStorage implements IExperienceStorage, INBTSerializable<CompoundNBT> {
    private int capacity;
    private int maxExtract;
    private int maxReceive;
    private IExperienceStorageChangedObserver observer;

    private float experience;
    private int experienceLevel;
    private int experienceTotal;

    public ExperienceStorage(int capacity, int current, int maxExtract, int maxReceive) {
        this.capacity = capacity;
        this.experienceTotal = current;
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
    }

    public ExperienceStorage(@Nonnull ExperienceBuffer buffer) {
        this.capacity = buffer.capacity;
        this.experienceTotal = buffer.current;
        this.maxExtract = buffer.maxExtract;
        this.maxReceive = buffer.maxReceive;
    }

    private void onExperienceChange() {
        if (this.observer != null) {
            this.observer.onExperienceChanged(this);
        }
    }

    public void setObserver(IExperienceStorageChangedObserver observer) {
        this.observer = observer;
    }

    public void giveExperience(int amount) {
        this.experience += (float)amount / (float)this.xpBarCap();
        this.experienceTotal = MathHelper.clamp(this.experienceTotal + amount, 0, this.capacity);

        while(this.experience < 0.0F) {
            float f = this.experience * (float)this.xpBarCap();
            if (this.experienceLevel > 0) {
                this.addExperienceLevel(-1);
                this.experience = 1.0F + f / (float)this.xpBarCap();
            } else {
                this.addExperienceLevel(-1);
                this.experience = 0.0F;
            }
        }

        while(this.experience >= 1.0F) {
            this.experience = (this.experience - 1.0F) * (float)this.xpBarCap();
            this.addExperienceLevel(1);
            this.experience /= (float)this.xpBarCap();
        }

    }

    private void addExperienceLevel(int levels) {
        this.experienceLevel += levels;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experience = 0.0F;
            this.experienceTotal = 0;
        }
    }

    private int xpBarCap() {
        if (this.experienceLevel >= 30) {
            return 112 + (this.experienceLevel - 30) * 9;
        } else {
            return this.experienceLevel >= 15 ? 37 + (this.experienceLevel - 15) * 5 : 7 + this.experienceLevel * 2;
        }
    }

//    public void setExperience(int current) {
//        final int old = this.current;
//        this.current = current;
//        this.onExperienceChange(old, this.current);
//    }

    public int getMaxExtract() {
        return maxExtract;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public int getCapacity() {
        return capacity;
    }

    public float getExperience() {
        return experience;
    }

    public int getExperienceLevel() {
        return experienceLevel;
    }

    public int getExperienceTotal() {
        return experienceTotal;
    }

    public void setExperienceTotal(int next) {
        if (next != this.experienceTotal) {
            int old = this.experienceTotal;
            int diff = next - old;
            giveExperience(diff);
            onExperienceChange();
        }
    }

    // ====---------------------------------------------------------------------------====
    // region IExperienceStorage

    @Override
    public int receiveExperience(int experience, boolean simulate) {
        int received = Math.min(experience, Math.min(maxReceive, capacity - experienceTotal));
        if (!simulate) {
            this.giveExperience(received);
            onExperienceChange();
        }

        return received;
    }

    @Override
    public int extractExperience(int experience, boolean simulate) {
        int extracted = Math.min(experience, Math.min(maxExtract, experienceTotal));
        if (!simulate) {
            this.giveExperience(-extracted);
            onExperienceChange();
        }

        return extracted;
    }

    @Override
    public int getExperienceStored() {
        return experienceTotal;
    }

    @Override
    public int getMaxExperienceStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region INBTSerializable

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("capacity", capacity);
        nbt.putInt("maxExtract", maxExtract);
        nbt.putInt("maxReceive", maxReceive);
        nbt.putFloat("experience", experience);
        nbt.putInt("experienceTotal", experienceTotal);
        nbt.putInt("experienceLevel", experienceLevel);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        capacity = nbt.getInt("capacity");
        maxExtract = nbt.getInt("maxExtract");
        maxReceive = nbt.getInt("maxReceive");
        experience = nbt.getFloat("experience");
        experienceTotal = nbt.getInt("experienceTotal");
        experienceLevel = nbt.getInt("experienceLevel");
    }

    public IProgress getLevelProgress() {
        return new IProgress() {
            @Override
            public int getProgressMin() {
                return 0;
            }

            @Override
            public int getProgressMax() {
                return 100;
            }

            @Override
            public int getProgressCurrent() {
                return (int) (experience * 100);
            }
        };
    }

    public IProgress getTotalProgress() {
        return new IProgress() {
            @Override
            public int getProgressMin() {
                return 0;
            }

            @Override
            public int getProgressMax() {
                return capacity;
            }

            @Override
            public int getProgressCurrent() {
                return experienceTotal;
            }
        };
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Observer

    public interface IExperienceStorageChangedObserver {
        void onExperienceChanged(@Nonnull ExperienceStorage storage);
    }

    // endregion
}
