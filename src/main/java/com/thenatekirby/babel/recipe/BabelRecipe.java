package com.thenatekirby.babel.recipe;

import com.thenatekirby.babel.core.container.EmptyContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class BabelRecipe implements Recipe<EmptyContainer> {
    private final ResourceLocation recipeId;

    public BabelRecipe(@Nonnull ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }

    // ====---------------------------------------------------------------------------====
    // region Getters & Setters

    public ResourceLocation getRecipeId() {
        return recipeId;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Recipe

    @Override
    public boolean matches(@Nonnull EmptyContainer inventory, @Nonnull Level level) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull EmptyContainer inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return recipeId;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return null;
    }

    // endregion
}
