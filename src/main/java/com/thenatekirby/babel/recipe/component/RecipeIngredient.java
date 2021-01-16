package com.thenatekirby.babel.recipe.component;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class RecipeIngredient {
    private boolean isTag;

    private String resultId;

    private int count;

    private RecipeIngredient(boolean isTag, String resultId, int count) {
        this.isTag = isTag;
        this.resultId = resultId;
        this.count = count;
    }

    public static RecipeIngredient fromTag(@Nonnull String resultId) {
        return new RecipeIngredient(true, resultId, 1);
    }

    public static RecipeIngredient fromTag(@Nonnull String resultId, int count) {
        return new RecipeIngredient(true, resultId, count);
    }

    public static RecipeIngredient fromItem(@Nonnull String resultId) {
        return new RecipeIngredient(false, resultId, 1);
    }

    public static RecipeIngredient fromItem(@Nonnull String resultId, int count) {
        return new RecipeIngredient(false, resultId, count);
    }

    @Nonnull
    public JsonObject serializeJson() {
        JsonObject jsonObject = new JsonObject();
        if (isTag) {
            jsonObject.addProperty("tag", this.resultId.toString());
        } else {
            jsonObject.addProperty("item", this.resultId.toString());
        }

        if (count > 1) {
            jsonObject.addProperty("count", this.count);
        }

        return jsonObject;
    }

    @Nonnull
    public ItemStack makeItemStack() {
        ResourceLocation id = new ResourceLocation(resultId);

        if (this.isTag) {
            ITag<Item> itemTag = ItemTags.getCollection().get(id);
            if (itemTag == null || itemTag.getAllElements().isEmpty()) {
                return ItemStack.EMPTY;
            }

            return new ItemStack(itemTag.getAllElements().get(0), count);

        } else {
            return new ItemStack(ForgeRegistries.ITEMS.getValue(id), count);
        }
    }

    public void write(@Nonnull PacketBuffer buffer) {
        buffer.writeBoolean(isTag);
        buffer.writeString(resultId);
        buffer.writeInt(count);
    }

    public static RecipeIngredient read(@Nonnull PacketBuffer buffer) {
        boolean isTag = buffer.readBoolean();
        String resultId = buffer.readString();
        int count = buffer.readInt();
        return new RecipeIngredient(isTag, resultId, count);
    }
}
