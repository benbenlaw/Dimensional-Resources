package com.benbenlaw.dimresources.worldgen;

import com.benbenlaw.dimresources.DimResources;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class DRBiomeModifiers {

    public static final ResourceKey<BiomeModifier> ADD_OVERWORLD_DIMENSIONAL_ORE =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, DimResources.identifier("add_overworld_dimensional_ore"));

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {

        HolderGetter<Biome> biomeHolderGetter = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatureHolderGetter = context.lookup(Registries.PLACED_FEATURE);

        HolderSet.Named<Biome> overworldBiomes = biomeHolderGetter.getOrThrow(BiomeTags.IS_OVERWORLD);

        context.register(ADD_OVERWORLD_DIMENSIONAL_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                overworldBiomes,
                HolderSet.direct(placedFeatureHolderGetter.getOrThrow(DRPlacedFeatures.OVERWORLD_DIMENSIONAL_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));

    }

}
