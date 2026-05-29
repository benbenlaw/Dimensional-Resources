package com.benbenlaw.dimresources.network;

import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.network.packet.SyncPlanetLocatorStack;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class DRMessages {

    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(DimResources.MOD_ID);

        //Client -> Server
        registrar.playToServer(SyncPlanetLocatorStack.TYPE, SyncPlanetLocatorStack.STREAM_CODEC, SyncPlanetLocatorStack.HANDLER);

    }
}
