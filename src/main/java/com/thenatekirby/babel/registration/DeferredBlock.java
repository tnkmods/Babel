package com.thenatekirby.babel.registration;

import com.thenatekirby.babel.api.IBlockProvider;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

// ====---------------------------------------------------------------------------====

public class DeferredBlock<T extends Block> implements IItemProvider, IBlockProvider {
    private final String name;
    private final RegistryObject<T> block;
    private final RegistryObject<Item> item;

    private DeferredBlock(String name, Supplier<T> blockSupplier, @Nullable Item.Properties properties, DeferredRegister<Block> blockRegister, DeferredRegister<Item> itemRegister) {
        this.name = name;
        this.block = blockRegister.register(name, blockSupplier);

        if (properties != null) {
            this.item = itemRegister.register(name, () -> new BlockItem(block.get(), properties));
        } else {
            this.item = null;
        }
    }

    public static <T extends Block> DeferredBlock<T> create(String name, Supplier<T> blockSupplier, Item.Properties itemProperties, DeferredRegister<Block> blockRegister, DeferredRegister<Item> itemRegister) {
        return new DeferredBlock<>(name, blockSupplier, itemProperties, blockRegister, itemRegister);
    }

    public static <T extends Block> DeferredBlock<T> createHidden(String name, Supplier<T> blockSupplier, DeferredRegister<Block> blockRegister, DeferredRegister<Item> itemRegister) {
        return new DeferredBlock<>(name, blockSupplier, null, blockRegister, itemRegister);
    }

    // ====---------------------------------------------------------------------------====
    // Getters

    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public T asBlock() {
        return block.get();
    }

    @Nonnull
    @Override
    public Item asItem() {
        return item.get();
    }
}
