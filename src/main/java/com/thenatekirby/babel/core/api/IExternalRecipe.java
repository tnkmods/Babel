package com.thenatekirby.babel.core.api;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public interface IExternalRecipe {
    @Nonnull
    ResourceLocation getId();

    @Nonnull
    ResourceLocation getType();

    void serialize(@Nonnull JsonObject json);

    default JsonObject getRecipeJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", getType().toString());
        serialize(jsonObject);
        return jsonObject;
    }
}
