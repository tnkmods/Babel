package com.thenatekirby.babel.mod;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.network.packet.ContainerUpdateGuiPacket;
import com.thenatekirby.babel.network.packet.ToggleRedstoneModePacket;

public class BabelPackets {
    public static void registerMessages() {
        Babel.NETWORK.register(ContainerUpdateGuiPacket.class, ContainerUpdateGuiPacket.Handler.INSTANCE);
        Babel.NETWORK.register(ToggleRedstoneModePacket.class, ToggleRedstoneModePacket.Handler.INSTANCE);
    }
}
