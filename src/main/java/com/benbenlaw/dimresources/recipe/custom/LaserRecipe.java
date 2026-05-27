package com.benbenlaw.dimresources.recipe.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record LaserRecipe(int rfPerTick, int duration, int laserLevel, Identifier planet, List<WeightedItemStack> output) implements Recipe<LaserRecipeInput> {

    public static final MapCodec<LaserRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("rf_per_tick").forGetter(LaserRecipe::rfPerTick),
            Codec.INT.fieldOf("duration").forGetter(LaserRecipe::duration),
            Codec.INT.fieldOf("laser_level").forGetter(LaserRecipe::laserLevel),
            Identifier.CODEC.fieldOf("planet").forGetter(LaserRecipe::planet),
            WeightedItemStack.CODEC.listOf().fieldOf("outputs").forGetter(LaserRecipe::output)
    ).apply(instance, LaserRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LaserRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, LaserRecipe::rfPerTick,
            ByteBufCodecs.INT, LaserRecipe::duration,
            ByteBufCodecs.INT, LaserRecipe::laserLevel,
            Identifier.STREAM_CODEC, LaserRecipe::planet,
            WeightedItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), LaserRecipe::output,
            LaserRecipe::new
    );

    public static final RecipeType<LaserRecipe> TYPE = new RecipeType<>() {};

    public static final RecipeSerializer<LaserRecipe> SERIALIZER =
            new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(LaserRecipeInput input, Level level) {
        if (input.getLaserLevel() < laserLevel) return false;
        return planet.equals(input.getPlanet());
    }

    public ItemStack rollOutput(RandomSource random) {
        int totalWeight = 0;

        for (WeightedItemStack entry : output) {
            totalWeight += entry.weight();
        }

        int roll = random.nextInt(totalWeight);

        for (WeightedItemStack entry : output) {
            roll -= entry.weight();
            if (roll < 0) {
                return entry.stack().create().copy();
            }
        }

        return ItemStack.EMPTY;
    }

    //Boiler Plate
    @Override
    public @NonNull ItemStack assemble(LaserRecipeInput recipeInput) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<? extends Recipe<LaserRecipeInput>> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<LaserRecipeInput>> getType() {
        return TYPE;
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public @NonNull String group() {
        return "";
    }
}
