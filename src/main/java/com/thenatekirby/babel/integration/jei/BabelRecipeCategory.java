package com.thenatekirby.babel.integration.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import com.thenatekirby.babel.core.api.IGuiRenderer;
import com.thenatekirby.babel.core.progress.IProgress;
import com.thenatekirby.babel.gui.GuiView;
import com.thenatekirby.babel.recipe.BabelRecipe;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

// ====---------------------------------------------------------------------------====

public class BabelRecipeCategory<T extends BabelRecipe, U extends BabelRecipeData> implements IRecipeCategory<T> {
    private IGuiHelper guiHelper;
    private IDrawable background;
    private IDrawable icon;

    private List<GuiView> subviews = new ArrayList<>();

    private final LoadingCache<T, U> storage;
    private final IGuiRenderer renderer;

    public BabelRecipeCategory(IGuiHelper guiHelper, int width, int height, Supplier<U> dataSupplier) {
        this(guiHelper, width, height, new CacheLoader<T, U>() {
            @Override
            public U load(@Nonnull T key) throws Exception {
                return dataSupplier.get();
            }
        });
    }

    public BabelRecipeCategory(IGuiHelper guiHelper, int width, int height, Function<T, U> cacheLoader) {
        this(guiHelper, width, height, new CacheLoader<T, U>() {
            @Override
            public U load(@Nonnull T key) throws Exception {
                return cacheLoader.apply(key);
            }
        });
    }

    protected BabelRecipeCategory(IGuiHelper guiHelper, int width, int height, CacheLoader<T, U> cacheLoader) {
        this.guiHelper = guiHelper;
        this.background = new EmptyJEIBackground(width, height);
        this.icon = makeIcon(guiHelper);
        this.renderer = new JeiGuiRenderer();

        storage = CacheBuilder.newBuilder()
                .maximumSize(20)
                .build(cacheLoader);

        initSubviews();
    }

    // ====---------------------------------------------------------------------------====
    // region Icon

    @Nonnull
    protected IDrawable makeIcon(IGuiHelper guiHelper) {
        return guiHelper.createDrawableIngredient(getIconItemStack());
    }

    @Nonnull
    protected ItemStack getIconItemStack() {
        return ItemStack.EMPTY;
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Data

    @Nonnull
    protected U getData(@Nonnull T recipe) {
        return storage.getUnchecked(recipe);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region Subviews

    protected void initSubviews() {
    }

    protected void addSubviews(GuiView... views) {
        Arrays.stream(views).forEach(view -> {
            view.setRenderer(renderer);
            subviews.add(view);
        });
    }

    protected IProgress makeProgress() {
        ITickTimer tickTimer = guiHelper.createTickTimer(20, 20, false);
        return new TimedProgress(tickTimer);
    }

    // endregion
    // ====---------------------------------------------------------------------------====
    // region IRecipeCategory

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        throw new RuntimeException("Babel Recipe Categories must override getUid()");
    }

    @Override
    @Nonnull
    public Class<? extends T> getRecipeClass() {
        throw new RuntimeException("Babel Recipe Categories must override getRecipeClass()");
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return new TextComponent("");
    }

    @Override
    @Nonnull
    public IDrawable getBackground() {
        return background;
    }

    @Override
    @Nonnull
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(@Nonnull T recipe, @Nonnull IIngredients ingredients) {
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull T recipe, @Nonnull IIngredients ingredients) {
    }

    @Override
    public void draw(@Nonnull T recipe, @Nonnull PoseStack matrixStack, double mouseX, double mouseY) {
        for (GuiView view : subviews) {
            view.render(matrixStack, (int)mouseX, (int)mouseY, 0);
        }
    }

    // endregion
}