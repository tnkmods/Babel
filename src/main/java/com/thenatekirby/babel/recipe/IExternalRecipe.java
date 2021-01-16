package com.thenatekirby.babel.recipe;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

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
