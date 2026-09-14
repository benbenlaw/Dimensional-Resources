package com.benbenlaw.dimresources.network.packet;

import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.loader.SkyObjectData;
import com.benbenlaw.dimresources.loader.SkyObjectLoader;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.HashMap;
import java.util.Map;

public record SyncSkyObjectsPayload(Map<Identifier, SkyObjectData> skyObjects) implements CustomPacketPayload {

    public static final Type<SyncSkyObjectsPayload> TYPE = new Type<>(DimResources.identifier("sync_sky_objects"));

    public static final IPayloadHandler<SyncSkyObjectsPayload> HANDLER = (packet, context) -> {
        context.player().level().getServer();
        context.enqueueWork(() -> {
            SkyObjectLoader.SKY_OBJECTS.clear();
            SkyObjectLoader.SKY_OBJECTS.putAll(packet.skyObjects());
        });
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncSkyObjectsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, SkyObjectData.STREAM_CODEC),
            SyncSkyObjectsPayload::skyObjects,
            SyncSkyObjectsPayload::new
    );

    @Override
    public Type<SyncSkyObjectsPayload> type() {
        return TYPE;
    }
}