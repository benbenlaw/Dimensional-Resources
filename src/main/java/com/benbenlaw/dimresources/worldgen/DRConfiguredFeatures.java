package com.benbenlaw.dimresources.worldgen;

import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.block.DRBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;


public class DRConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_DIMENSIONAL_ORE_KEY = createKey("overworld_dimensional_ore");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        RuleTest stoneOreReplaceables =  new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreReplaceables =  new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        FeatureUtils.register(context, OVERWORLD_DIMENSIONAL_ORE_KEY,
                Feature.ORE,
                new OreConfiguration(
                        List.of(
                                OreConfiguration.target(stoneOreReplaceables, DRBlocks.DIMENSIONAL_ORE.get().defaultBlockState()),
                                OreConfiguration.target(deepslateOreReplaceables, DRBlocks.DEEPSLATE_DIMENSIONAL_ORE.get().defaultBlockState()))
                        ,4));

    }

    private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, DimResources.identifier(name));
    }

}
