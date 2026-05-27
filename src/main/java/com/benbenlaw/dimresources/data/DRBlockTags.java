package com.benbenlaw.dimresources.data;

import com.benbenlaw.core.tag.CommonTags;
import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.block.DRBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DRBlockTags extends BlockTagsProvider {

    DRBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, DimResources.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        tag(CommonTags.getBlockTag("c", "ores/dimensional"))
                .add(DRBlocks.DIMENSIONAL_ORE.get())
                .add(DRBlocks.DEEPSLATE_DIMENSIONAL_ORE.get())
        ;

        tag(Tags.Blocks.ORES).add(DRBlocks.DIMENSIONAL_ORE.get()).add(DRBlocks.DEEPSLATE_DIMENSIONAL_ORE.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(DRBlocks.DIMENSIONAL_ORE.get())
                .add(DRBlocks.DEEPSLATE_DIMENSIONAL_ORE.get())
                .add(DRBlocks.LASER.get())
                .add(DRBlocks.DIMENSIONAL_STONE.get())
                .add(DRBlocks.DIMENSIONAL_STONE_STAIRS.get())
                .add(DRBlocks.DIMENSIONAL_STONE_SLAB.get())
                .add(DRBlocks.DIMENSIONAL_STONE_WALL.get())
                .add(DRBlocks.DIMENSIONAL_STONE_BRICKS.get())
                .add(DRBlocks.DIMENSIONAL_STONE_BRICK_STAIRS.get())
                .add(DRBlocks.DIMENSIONAL_STONE_BRICK_SLAB.get())
                .add(DRBlocks.DIMENSIONAL_STONE_BRICK_WALL.get())
        ;

        tag(BlockTags.STAIRS).add(DRBlocks.DIMENSIONAL_STONE_BRICK_STAIRS.get(), DRBlocks.DIMENSIONAL_STONE_BRICK_WALL.get());
        tag(BlockTags.SLABS).add(DRBlocks.DIMENSIONAL_STONE_SLAB.get(), DRBlocks.DIMENSIONAL_STONE_BRICK_WALL.get());
        tag(BlockTags.WALLS).add(DRBlocks.DIMENSIONAL_STONE_WALL.get(), DRBlocks.DIMENSIONAL_STONE_BRICK_WALL.get());
    }

}
