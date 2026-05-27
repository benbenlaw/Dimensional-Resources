package com.benbenlaw.dimresources.worldgen;

import com.benbenlaw.dimresources.DimResources;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class DRPlacedFeatures {

    public static final ResourceKey<PlacedFeature> OVERWORLD_DIMENSIONAL_ORE_PLACED_KEY = createKey("overworld_dimensional_ore_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);

        Holder<ConfiguredFeature<?, ?>> overworldDimensionalOreConfiguredFeature =
                holdergetter.getOrThrow(DRConfiguredFeatures.OVERWORLD_DIMENSIONAL_ORE_KEY);

        PlacementUtils.register(context, OVERWORLD_DIMENSIONAL_ORE_PLACED_KEY, holdergetter.getOrThrow(DRConfiguredFeatures.OVERWORLD_DIMENSIONAL_ORE_KEY),
                commonOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-16))));


    }

    private static List<PlacementModifier> orePlacement(PlacementModifier modifier, PlacementModifier modifier1) {
        return List.of(modifier, InSquarePlacement.spread(), modifier1, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
        return orePlacement(CountPlacement.of(count), modifier);
    }

    private static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, DimResources.identifier(name));
    }
}
