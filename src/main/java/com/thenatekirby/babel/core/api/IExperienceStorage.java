package com.thenatekirby.babel.core.api;

public interface IExperienceStorage {
    int receiveExperience(int experience, boolean simulate);

    int extractExperience(int experience, boolean simulate);

    int getExperienceStored();

    int getMaxExperienceStored();

    boolean canExtract();

    boolean canReceive();
}
