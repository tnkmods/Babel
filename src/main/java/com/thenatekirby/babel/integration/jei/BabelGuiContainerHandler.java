package com.thenatekirby.babel.integration.jei;

import com.thenatekirby.babel.machine.gui.DeviceScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;

import javax.annotation.Nonnull;
import java.util.List;

// ====---------------------------------------------------------------------------====

public class BabelGuiContainerHandler implements IGuiContainerHandler<DeviceScreen<?>> {
    @Nonnull
    @Override
    public List<Rect2i> getGuiExtraAreas(DeviceScreen<?> containerScreen) {
        return containerScreen.getExtraBounds();
    }
}
