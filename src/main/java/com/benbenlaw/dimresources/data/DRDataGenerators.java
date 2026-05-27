package com.benbenlaw.dimresources.data;


import com.benbenlaw.dimresources.DimResources;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = DimResources.MOD_ID)
public class DRDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {

        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new DRBlockTags(packOutput, lookupProvider));
        generator.addProvider(true, new DRItemTags(packOutput, lookupProvider));
        generator.addProvider(true, new DRLangProvider(packOutput));
        generator.addProvider(true, new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(DRLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));
        generator.addProvider(true, new DRModelProvider(packOutput));
        generator.addProvider(true, new DRWorldGenProvider(packOutput, lookupProvider));

        //Recipes
        generator.addProvider(true, new DRRecipeProvider.Runner(packOutput, lookupProvider));



    }


}
