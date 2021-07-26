package com.thenatekirby.babel.core;

import com.thenatekirby.babel.api.IProgress;

public class ConstantProgress implements IProgress {
    private final int current;
    private final int max;

    public ConstantProgress(int current) {
        this(current, 100);
    }

    public ConstantProgress(int current, int max) {
        this.current = current;
        this.max = max;
    }

    @Override
    public int getProgressMin() {
        return 0;
    }

    @Override
    public int getProgressMax() {
        return max;
    }

    @Override
    public int getProgressCurrent() {
        return current;
    }
}
