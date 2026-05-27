package com.benbenlaw.dimresources.data;

import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.worldgen.DRBiomeModifiers;
import com.benbenlaw.dimresources.worldgen.DRConfiguredFeatures;
import com.benbenlaw.dimresources.worldgen.DRPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DRWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public DRWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, DRConfiguredFeatures::bootstrap)
                .add(Registries.PLACED_FEATURE, DRPlacedFeatures::bootstrap)
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, DRBiomeModifiers::bootstrap),
                Set.of(DimResources.MOD_ID));
    }

}
