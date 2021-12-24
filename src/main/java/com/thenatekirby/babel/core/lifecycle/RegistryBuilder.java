package com.thenatekirby.babel.core.lifecycle;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

// ====---------------------------------------------------------------------------====

public class RegistryBuilder {
    public void addRecipeSerializers(DeferredRegister<RecipeSerializer<?>> register) {
        register.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public void addItems(DeferredRegister<Item> register) {
        register.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public void addBlocks(DeferredRegister<Block> register) {
        register.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public void addBlockEntities(DeferredRegister<BlockEntityType<?>> register) {
        register.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public void addContainers(DeferredRegister<MenuType<?>> register) {
        register.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
