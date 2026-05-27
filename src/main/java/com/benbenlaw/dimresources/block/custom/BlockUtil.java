package com.benbenlaw.dimresources.block.custom;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockUtil {

    public void registerBlockFamily(String blockName, DeferredRegister.Blocks registry) {

        Block block = new Block(BlockBehaviour.Properties.of());
        StairBlock stairBlock = new StairBlock(block.defaultBlockState(), BlockBehaviour.Properties.of());
        SlabBlock slabBlock = new SlabBlock(BlockBehaviour.Properties.of());
        WallBlock wallBlock = new WallBlock(BlockBehaviour.Properties.of());


        registry.register(block + "_stairs", () -> stairBlock);
        registry.register(block + "_slab", () -> slabBlock);
        registry.register(block + "_wall", () -> wallBlock);
        registry.register(block + "", () -> block);





    }



}
