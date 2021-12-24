package com.thenatekirby.babel.registration;

import com.thenatekirby.babel.core.api.IBlockProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

// ====---------------------------------------------------------------------------====

public class DeferredBlockEntity<T extends Block, U extends BlockEntity> implements IBlockProvider {
    private final String name;
    private final RegistryObject<T> block;
    private final RegistryObject<BlockEntityType<U>> tileEntity;
    private final RegistryObject<Item> item;

    public DeferredBlockEntity(String name, Supplier<T> blockSupplier, BlockEntityType.BlockEntitySupplier<U> entitySupplier, Item.Properties itemProperties, DeferredRegister<Block> blockRegister, DeferredRegister<Item> itemRegister, DeferredRegister<BlockEntityType<?>> entityRegister) {
        this.name = name;

        this.block = blockRegister.register(name, blockSupplier);
        this.tileEntity = entityRegister.register(name, () -> BlockEntityType.Builder.of(entitySupplier, block.get()).build(null));
        this.item = itemRegister.register(name, () -> new BlockItem(block.get(), itemProperties));
    }

    public static <T extends Block, U extends BlockEntity> DeferredBlockEntity<T, U> create(String name, Supplier<T> blockSupplier, BlockEntityType.BlockEntitySupplier<U> entitySupplier, Item.Properties itemProperties, DeferredRegister<Block> blockRegister, DeferredRegister<Item> itemRegister, DeferredRegister<BlockEntityType<?>> entityRegister) {
        return new DeferredBlockEntity<>(name, blockSupplier, entitySupplier, itemProperties, blockRegister, itemRegister, entityRegister);
    }

    // ====---------------------------------------------------------------------------====
    // region Getters

    public String getName() {
        return name;
    }

    @Nonnull
    public T asBlock() {
        return block.get();
    }

    public BlockEntityType<U> asTileEntityType() {
        return tileEntity.get();
    }

    public Item asItem() {
        return item.get();
    }

    // endregion
}
