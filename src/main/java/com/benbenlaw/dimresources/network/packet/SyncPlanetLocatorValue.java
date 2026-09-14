package com.benbenlaw.dimresources.network.packet;

import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.item.DRDataComponent;
import com.benbenlaw.dimresources.item.PlanetLocatorItem;
import com.benbenlaw.dimresources.loader.SkyObjectLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record SyncPlanetLocatorValue(Identifier planet) implements CustomPacketPayload {

    public static final Type<SyncPlanetLocatorValue> TYPE = new Type<>(DimResources.identifier("update_planet_locator_stack"));

    public static final IPayloadHandler<SyncPlanetLocatorValue> HANDLER = (packet, context) -> {
        context.player().level().getServer().execute(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;

            ItemStack stack = player.getMainHandItem();
            if (!(stack.getItem() instanceof PlanetLocatorItem)) {
                stack = player.getOffhandItem();
                if (!(stack.getItem() instanceof PlanetLocatorItem)) return;
            }

            Identifier planet = packet.planet();
            if (planet == null) return;

            if (!isValidPlanet(planet)) return;

            stack.set(DRDataComponent.PLANET.get(), planet);
        });
    };

    private static boolean isValidPlanet(Identifier planet) {
        return SkyObjectLoader.SKY_OBJECTS.containsKey(planet);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPlanetLocatorValue> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, SyncPlanetLocatorValue::planet,
            SyncPlanetLocatorValue::new
    );

    @Override
    public Type<SyncPlanetLocatorValue> type() {
        return TYPE;
    }
}