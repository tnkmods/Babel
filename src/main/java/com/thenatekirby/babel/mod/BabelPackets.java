package com.thenatekirby.babel.mod;

import com.thenatekirby.babel.Babel;
import com.thenatekirby.babel.network.NetworkManager;
import com.thenatekirby.babel.network.packet.ContainerUpdateGuiPacket;

public class BabelPackets {
    public static void registerMessages() {
        Babel.NETWORK.register(ContainerUpdateGuiPacket.class, ContainerUpdateGuiPacket.Handler.INSTANCE);
    }
}
