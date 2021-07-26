package com.thenatekirby.babel.registration;

import com.thenatekirby.babel.api.IBlockProvider;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

// ====---------------------------------------------------------------------------====

public class DeferredTileEntity<T extends Block, U extends TileEntity> implements IBlockProvider {
    private final String name;
    private final RegistryObject<T> block;
    private final RegistryObject<TileEntityType<U>> tileEntity;
    private final RegistryObject<Item> item;

    public DeferredTileEntity(String name, Supplier<T> blockSupplier, Supplier<U> tileEntitySupplier, Item.Properties itemProperties, DeferredRegister<Block> blockRegister, DeferredRegister<Item> itemRegister, DeferredRegister<TileEntityType<?>> tileEntityRegister) {
        this.name = name;

        this.block = blockRegister.register(name, blockSupplier);
        this.tileEntity = tileEntityRegister.register(name, () -> TileEntityType.Builder.create(tileEntitySupplier, block.get()).build(null));
        this.item = itemRegister.register(name, () -> new BlockItem(block.get(), itemProperties));
    }

    public static <T extends Block, U extends TileEntity> DeferredTileEntity<T, U> create(String name, Supplier<T> blockSupplier, Supplier<U> tileEntitySupplier, Item.Properties itemProperties, DeferredRegister<Block> blockRegister, DeferredRegister<Item> itemRegister, DeferredRegister<TileEntityType<?>> tileEntityRegister) {
        return new DeferredTileEntity<>(name, blockSupplier, tileEntitySupplier, itemProperties, blockRegister, itemRegister, tileEntityRegister);
    }

    // ====---------------------------------------------------------------------------====
    // Getters

    public String getName() {
        return name;
    }

    @Nonnull
    public T asBlock() {
        return block.get();
    }

    public TileEntityType<U> asTileEntityType() {
        return tileEntity.get();
    }

    public Item asItem() {
        return item.get();
    }
}
