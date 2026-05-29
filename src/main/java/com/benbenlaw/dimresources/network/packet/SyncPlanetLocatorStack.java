package com.benbenlaw.dimresources.network.packet;

import com.benbenlaw.dimresources.DimResources;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record SyncPlanetLocatorStack(ItemStack planetLocator) implements CustomPacketPayload {

    public static final Type<SyncPlanetLocatorStack> TYPE = new Type<>(DimResources.identifier("update_planet_locator_stack"));

    public static final IPayloadHandler<SyncPlanetLocatorStack> HANDLER = (packet, context) -> {

        context.player().setItemInHand(InteractionHand.OFF_HAND, packet.planetLocator);
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPlanetLocatorStack> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, SyncPlanetLocatorStack::planetLocator,
            SyncPlanetLocatorStack::new
    );

    @Override
    public Type<SyncPlanetLocatorStack> type() {
        return TYPE;
    }
}