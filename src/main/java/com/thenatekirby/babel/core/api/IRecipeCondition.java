package com.thenatekirby.babel.core.api;

import com.google.gson.JsonObject;
import net.minecraftforge.common.crafting.conditions.ICondition;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

/**
 * An interface for an ICondition that is also serializable to a JsonObject via
 * it's serializeJson() method.
 */

public interface IRecipeCondition extends ICondition {
    @Nonnull
    JsonObject serializeJson();
}
