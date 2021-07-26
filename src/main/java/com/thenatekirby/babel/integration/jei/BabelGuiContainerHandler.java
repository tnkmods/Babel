package com.thenatekirby.babel.integration.jei;

import com.thenatekirby.babel.core.gui.BabelContainerScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.Rectangle2d;

import java.util.List;

public class BabelGuiContainerHandler implements IGuiContainerHandler<BabelContainerScreen<?>> {
    @Override
    public List<Rectangle2d> getGuiExtraAreas(BabelContainerScreen<?> containerScreen) {
        return containerScreen.getExtraBounds();
    }
}
