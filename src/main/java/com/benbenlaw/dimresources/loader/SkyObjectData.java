package com.benbenlaw.dimresources.loader;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.List;

public record SkyObjectData(Identifier texture, int beamColor, float beamRadius, float size, float distance,
                            float rotationX, float rotationZ,
                            float speedX, float speedZ,
                            List<Identifier> excludedDimensions) {

    public static final Codec<SkyObjectData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("texture").forGetter(SkyObjectData::texture),
                    Codec.INT.fieldOf("beam_color").forGetter(SkyObjectData::beamColor),
                    Codec.FLOAT.fieldOf("beam_radius").forGetter(SkyObjectData::beamRadius),
                    Codec.FLOAT.fieldOf("size").forGetter(SkyObjectData::size),
                    Codec.FLOAT.fieldOf("distance").forGetter(SkyObjectData::distance),
                    Codec.FLOAT.fieldOf("rotation_x").forGetter(SkyObjectData::rotationX),
                    Codec.FLOAT.fieldOf("rotation_z").forGetter(SkyObjectData::rotationZ),
                    Codec.FLOAT.fieldOf("speed_x").forGetter(SkyObjectData::speedX),
                    Codec.FLOAT.fieldOf("speed_z").forGetter(SkyObjectData::speedZ),
                    Identifier.CODEC.listOf().optionalFieldOf("excluded_dimensions", List.of()).forGetter(SkyObjectData::excludedDimensions)
            ).apply(instance, SkyObjectData::new)
    );

    public static final StreamCodec<FriendlyByteBuf, SkyObjectData> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, SkyObjectData::texture,
            ByteBufCodecs.INT, SkyObjectData::beamColor,
            ByteBufCodecs.FLOAT, SkyObjectData::beamRadius,
            ByteBufCodecs.FLOAT, SkyObjectData::size,
            ByteBufCodecs.FLOAT, SkyObjectData::distance,
            ByteBufCodecs.FLOAT, SkyObjectData::rotationX,
            ByteBufCodecs.FLOAT, SkyObjectData::rotationZ,
            ByteBufCodecs.FLOAT, SkyObjectData::speedX,
            ByteBufCodecs.FLOAT, SkyObjectData::speedZ,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()), SkyObjectData::excludedDimensions,
            SkyObjectData::new
    );

    public boolean isValidIn(Identifier dimensionId) {
        return !excludedDimensions.contains(dimensionId);
    }
}