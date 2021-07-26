package com.thenatekirby.babel.condition;

import com.google.gson.JsonObject;
import com.thenatekirby.babel.core.MutableResourceLocation;
import com.thenatekirby.babel.integration.Mods;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

/**
 * A generator of Forge's own Mod Loaded Condition for serializing into our recipe builders because
 * it conforms to our own IRecipeCondition. It shouldn't be used as a condition for recipe loading.
 * (In fact, it won't work - the test() method just returns false, intentionally).
 */

public class ModLoadedCondition implements IRecipeCondition {
    private final String modId;

    public ModLoadedCondition(@Nonnull String modId) {
        this.modId = modId;
    }

    public ModLoadedCondition(@Nonnull MutableResourceLocation resourceLocation) {
        this(resourceLocation.getRoot());
    }

    @Nonnull
    @Override
    public JsonObject serializeJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "forge:mod_loaded");
        jsonObject.addProperty("modid", this.modId);
        return jsonObject;
    }

    @Override
    public ResourceLocation getID() {
        return Mods.FORGE.withPath("mod_loaded");
    }

    @Override
    public boolean test() {
        return false;
    }
}
