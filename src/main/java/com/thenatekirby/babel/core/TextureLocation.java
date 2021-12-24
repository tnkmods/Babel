package com.thenatekirby.babel.core;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class TextureLocation {
    public enum ResourceType {
        GUI
    }

    private static String resourcePathFromResourceType(@Nonnull ResourceType resourceType) {
        switch (resourceType) {
            case GUI: {
                return "textures/gui/";
            }
        }

        return "";
    }

    public static ResourceLocation getResourceLocation(@Nonnull String modId, @Nonnull ResourceType resourceType, @Nonnull String path) {
        return new ResourceLocation(modId, resourcePathFromResourceType(resourceType) + path);
    }
}
