package com.thenatekirby.babel.core;

import com.thenatekirby.babel.api.IProgress;

public class InvertedProgress implements IProgress {
    private final IProgress progress;

    public InvertedProgress(IProgress progress) {
        this.progress = progress;
    }

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
}
