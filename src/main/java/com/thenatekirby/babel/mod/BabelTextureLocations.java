package com.thenatekirby.babel.mod;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.core.TextureLocation;
import net.minecraft.util.ResourceLocation;

public class BabelTextureLocations {
    public static class GUI {
        public static final ResourceLocation GUI_BLANK = TextureLocation.getResourceLocation(Babel.MOD_ID, TextureLocation.ResourceType.GUI, "gui_blank.png");
        public static final ResourceLocation COMPONENTS = TextureLocation.getResourceLocation(Babel.MOD_ID, TextureLocation.ResourceType.GUI, "gui_components.png");
    }
}
