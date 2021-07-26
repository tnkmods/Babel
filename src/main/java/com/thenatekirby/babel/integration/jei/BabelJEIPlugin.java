package com.thenatekirby.babel.integration.jei;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.core.gui.BabelContainerScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class BabelJEIPlugin implements IModPlugin {
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(BabelContainerScreen.class, new BabelGuiContainerHandler());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return Babel.MOD.withPath("jei");
    }
}
