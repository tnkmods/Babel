package com.thenatekirby.babel.core;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class MutableResourceLocation {
    @Nonnull
    private final String root;

    public MutableResourceLocation(@Nonnull String root) {
        this.root = root;
    }

    public ResourceLocation withPath(@Nonnull String path) {
        return new ResourceLocation(root, path);
    }

    @Nonnull
    public String getRoot() {
        return root;
    }
}
