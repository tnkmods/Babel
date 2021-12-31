package com.thenatekirby.babel.integration.jei;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.machine.gui.DeviceScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

@JeiPlugin
public class BabelJEIPlugin implements IModPlugin {
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(DeviceScreen.class, new BabelGuiContainerHandler());
    }

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return Babel.MOD.withPath("jei");
    }
}
