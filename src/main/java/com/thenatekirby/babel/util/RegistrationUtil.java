package com.thenatekirby.babel.util;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.api.IBlockReplacement;
import com.thenatekirby.babel.mixin.StateHolderMixin;
import com.thenatekirby.babel.mixin.StateContainerMixin;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Objects;

// ====---------------------------------------------------------------------------====

/**
 * Helper methods for registration not covered elsewhere.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class RegistrationUtil {
    public static boolean overrideExistingBlock(@Nonnull Block block, String modId) {
        Block existing = ForgeRegistries.BLOCKS.getValue(block.getRegistryName());
        if (existing == null) {
            Babel.getLogger().error("Unable to find block {}", block.getRegistryName());
            return false;
        }

        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(new BlockItem(block, new Item.Properties().tab(Objects.requireNonNull(existing.asItem().getItemCategory()))) {
            @Override
            public String getCreatorModId(ItemStack itemStack) {
                return modId;
            }

        }.setRegistryName(Objects.requireNonNull(block.getRegistryName())));
        return true;
    }

    @SuppressWarnings("unchecked")
    public static <B extends Block & IBlockReplacement> void overrideBlockstates(@Nonnull Block oldBlock, @Nonnull B newBlock) {
        newBlock.overrideStateContainer(oldBlock.getStateDefinition());
        newBlock.overrideDefaultState(oldBlock.defaultBlockState());

        ((StateContainerMixin<Block>) newBlock.getStateDefinition()).setOwner(newBlock);
        newBlock.getStateDefinition().getPossibleStates().forEach(blockState -> ((StateHolderMixin) blockState).setInstance(newBlock));
    }
}
