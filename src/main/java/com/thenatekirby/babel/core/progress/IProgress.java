package com.thenatekirby.babel.core.progress;

// ====---------------------------------------------------------------------------====

public interface IProgress {
    int getProgressMin();
    int getProgressMax();
    int getProgressCurrent();

    default float getProgress() {
        return ((float) getProgressCurrent() / ((float) (getProgressMax() - getProgressMin())));
    }
}
