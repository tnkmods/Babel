package com.thenatekirby.babel.core.progress;

// ====---------------------------------------------------------------------------====

public class InvertedProgress implements IProgress {
    private final IProgress progress;

    public InvertedProgress(IProgress progress) {
        this.progress = progress;
    }

    // ====---------------------------------------------------------------------------====
    // region IProgress

    @Override
    public int getProgressMin() {
        return progress.getProgressMin();
    }

    @Override
    public int getProgressMax() {
        return progress.getProgressMax();
    }

    @Override
    public int getProgressCurrent() {
        return progress.getProgressMax() - progress.getProgressCurrent();
    }

    // endregion
}
