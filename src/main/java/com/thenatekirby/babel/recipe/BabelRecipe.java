package com.thenatekirby.babel.recipe;

import com.thenatekirby.babel.core.EmptyInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BabelRecipe implements IRecipe<EmptyInventory> {
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
    // region IRecipe

    @Override
    public boolean matches(@Nonnull EmptyInventory inv, @Nonnull World worldIn) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull EmptyInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public IRecipeType<?> getType() {
        return null;
    }
    // endregion
}
