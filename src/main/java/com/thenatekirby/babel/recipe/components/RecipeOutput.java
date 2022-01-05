package com.thenatekirby.babel.recipe.components;

import com.thenatekirby.babel.core.api.IItemProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class RecipeOutput {
    private boolean tag;
    private String resultId;
    private int count;
    private float chance = 1.0f;

    private RecipeOutput(boolean tag, String resultId, int count) {
        this.tag = tag;
        this.resultId = resultId;
        this.count = count;
    }

    public RecipeOutput withChance(float chance) {
        this.chance = chance;
        return this;
    }

    // ====---------------------------------------------------------------------------====
    // region Tags

    public static RecipeOutput fromTag(@Nonnull String resultId) {
        return new RecipeOutput(true, resultId, 1);
    }

    public static RecipeOutput fromTag(@Nonnull String resultId, int count) {
        return new RecipeOutput(true, resultId, count);
    }

    public static RecipeOutput fromTag(@Nonnull ResourceLocation resultId) {
        return new RecipeOutput(true, resultId.toString(), 1);
    }

    public static RecipeOutput fromTag(@Nonnull ResourceLocation resultId, int count) {
        return new RecipeOutput(true, resultId.toString(), count);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Items

    public static RecipeOutput fromItem(@Nonnull String resultId) {
        return new RecipeOutput(false, resultId, 1);
    }

    public static RecipeOutput fromItem(@Nonnull String resultId, int count) {
        return new RecipeOutput(false, resultId, count);
    }

    public static RecipeOutput fromItem(@Nonnull ResourceLocation resultId) {
        return new RecipeOutput(false, resultId.toString(), 1);
    }

    public static RecipeOutput fromItem(@Nonnull ResourceLocation resultId, int count) {
        return new RecipeOutput(false, resultId.toString(), count);
    }

    public static RecipeOutput fromItem(@Nonnull IItemProvider item) {
        return new RecipeOutput(false, item.asItem().getRegistryName().toString(), 1);
    }

    public static RecipeOutput fromItem(@Nonnull IItemProvider item, int count) {
        return new RecipeOutput(false, item.asItem().getRegistryName().toString(), count);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // Region Getters

    public boolean isTag() {
        return tag;
    }

    public String getResultId() {
        return resultId;
    }

    public int getCount() {
        return count;
    }

    public float getChance() {
        return chance;
    }

    // endregion
}
