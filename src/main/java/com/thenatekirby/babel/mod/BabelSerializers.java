package com.thenatekirby.babel.mod;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.recipe.TaggedShapelessCraftingRecipe;
import com.thenatekirby.babel.recipe.TaggedSmeltingRecipe;
import com.thenatekirby.babel.registration.DeferredRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BabelSerializers {
    private static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Babel.MOD_ID);

    public static final DeferredRecipeSerializer<TaggedShapelessCraftingRecipe.Serializer> TAGGED_SHAPELESS = DeferredRecipeSerializer.create(TaggedShapelessCraftingRecipe.RECIPE_TYPE_NAME, TaggedShapelessCraftingRecipe.Serializer::new, RECIPE_SERIALIZERS);
    public static final DeferredRecipeSerializer<TaggedSmeltingRecipe.Serializer> TAGGED_SMELTING = DeferredRecipeSerializer.create(TaggedSmeltingRecipe.RECIPE_TYPE_NAME, TaggedSmeltingRecipe.Serializer::new, RECIPE_SERIALIZERS);
    public static void register() {
        RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
