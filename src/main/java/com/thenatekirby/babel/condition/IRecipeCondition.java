package com.thenatekirby.babel.condition;

import com.google.gson.JsonObject;
import net.minecraftforge.common.crafting.conditions.ICondition;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

/**
 * An interface denoting an ICondition that is also serializable to a JsonObject via
 * it's serializeJson() method.
 */

public interface IRecipeCondition extends ICondition {
    @Nonnull
    JsonObject serializeJson();
}
