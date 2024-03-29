package com.thenatekirby.babel.babelmod;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.recipe.TaggedShapelessCraftingRecipe;
import com.thenatekirby.babel.recipe.TaggedSmeltingRecipe;
import com.thenatekirby.babel.registration.DeferredRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

// ====---------------------------------------------------------------------------====

public class BabelSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Babel.MOD_ID);

    public static final DeferredRecipeSerializer<TaggedShapelessCraftingRecipe.Serializer> TAGGED_SHAPELESS = DeferredRecipeSerializer.create(TaggedShapelessCraftingRecipe.RECIPE_TYPE_NAME, TaggedShapelessCraftingRecipe.Serializer::new, RECIPE_SERIALIZERS);
    public static final DeferredRecipeSerializer<TaggedSmeltingRecipe.Serializer> TAGGED_SMELTING = DeferredRecipeSerializer.create(TaggedSmeltingRecipe.RECIPE_TYPE_NAME, TaggedSmeltingRecipe.Serializer::new, RECIPE_SERIALIZERS);
}
