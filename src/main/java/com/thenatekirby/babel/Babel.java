package com.thenatekirby.babel;

import com.thenatekirby.babel.mod.BabelSerializers;
import com.thenatekirby.babel.core.MutableResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// ====---------------------------------------------------------------------------====

@Mod("babel")
public class Babel {
    private static final Logger LOGGER = LogManager.getLogger();
    public static Logger getLogger() {
        return LOGGER;
    }

    public static final String MOD_ID = "babel";
    public static final MutableResourceLocation MOD = new MutableResourceLocation(MOD_ID);

    public Babel() {
        MinecraftForge.EVENT_BUS.register(this);

        BabelSerializers.register();
    }
}
