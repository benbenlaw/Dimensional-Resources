package com.benbenlaw.dimresources.data;

import com.benbenlaw.core.tag.CommonTags;
import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.block.DRBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DRItemTags extends ItemTagsProvider {

    public DRItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, DimResources.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        tag(CommonTags.getItemTag("c", "ores/dimensional"))
                .add(DRBlocks.DIMENSIONAL_ORE.get().asItem())
                .add(DRBlocks.DEEPSLATE_DIMENSIONAL_ORE.get().asItem())
        ;

        tag(Tags.Items.ORES).add(DRBlocks.DIMENSIONAL_ORE.get().asItem()).add(DRBlocks.DEEPSLATE_DIMENSIONAL_ORE.get().asItem());

        tag(ItemTags.STAIRS).add(DRBlocks.DIMENSIONAL_STONE_BRICK_STAIRS.get().asItem(), DRBlocks.DIMENSIONAL_STONE_BRICK_WALL.get().asItem());
        tag(ItemTags.SLABS).add(DRBlocks.DIMENSIONAL_STONE_SLAB.get().asItem(), DRBlocks.DIMENSIONAL_STONE_BRICK_WALL.get().asItem());
        tag(ItemTags.WALLS).add(DRBlocks.DIMENSIONAL_STONE_WALL.get().asItem(), DRBlocks.DIMENSIONAL_STONE_BRICK_WALL.get().asItem());

    }
}
