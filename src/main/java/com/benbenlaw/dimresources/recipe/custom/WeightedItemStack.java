package com.benbenlaw.dimresources.recipe.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

//POSSIBLE MOVE TO CORE AS MAYBE USEFUL IN OTHER MODS

public record WeightedItemStack(ItemStackTemplate stack, int weight) {

    public ItemStackTemplate getStack() {
        return stack;
    }

    public int getWeight() {
        return weight;
    }

    public static Codec<WeightedItemStack> CODEC =  RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemStackTemplate.CODEC.fieldOf("stack").forGetter(WeightedItemStack::getStack),
                Codec.INT.fieldOf("weight").forGetter(WeightedItemStack::getWeight)
        ).apply(instance, WeightedItemStack::new));

    public static StreamCodec<RegistryFriendlyByteBuf, WeightedItemStack> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC, WeightedItemStack::getStack,
            ByteBufCodecs.INT, WeightedItemStack::getWeight,
            WeightedItemStack::new
    );

}