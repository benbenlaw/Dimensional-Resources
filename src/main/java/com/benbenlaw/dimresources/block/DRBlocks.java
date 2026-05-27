package com.benbenlaw.dimresources.block;

import com.benbenlaw.core.block.SyncableBlock;
import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.block.custom.DimensionalOreBlock;
import com.benbenlaw.dimresources.block.custom.LaserBlock;
import com.benbenlaw.dimresources.item.DRItems;
import net.minecraft.data.BlockFamilies;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class DRBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DimResources.MOD_ID);

    public static final DeferredBlock<Block> DIMENSIONAL_ORE = registerBlock("dimensional_ore",
            properties -> new DimensionalOreBlock(properties
                    .strength(3.0F, 3.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));

    public static final DeferredBlock<Block> DEEPSLATE_DIMENSIONAL_ORE = registerBlock("deepslate_dimensional_ore",
            properties -> new DimensionalOreBlock(properties
                    .strength(4.5f, 3.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));

    public static final DeferredBlock<Block> LASER = registerBlock("laser",
            properties -> new LaserBlock(properties
                    .strength(4.5f, 3.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(SyncableBlock.RUNNING) ? 15 : 0)
                    .noOcclusion()));

    public static final DeferredBlock<Block> DIMENSIONAL_STONE = registerBlock("dimensional_stone",
            properties -> new Block(blockFamilyProperties(properties)));

    public static final DeferredBlock<Block> DIMENSIONAL_STONE_STAIRS = registerBlock("dimensional_stone_stairs",
            properties -> new StairBlock(DRBlocks.DIMENSIONAL_STONE.get().defaultBlockState(), blockFamilyProperties(properties)));

    public static final DeferredBlock<Block> DIMENSIONAL_STONE_SLAB = registerBlock("dimensional_stone_slab",
            properties -> new SlabBlock(blockFamilyProperties(properties)));

    public static final DeferredBlock<Block> DIMENSIONAL_STONE_WALL = registerBlock("dimensional_stone_wall",
            properties -> new WallBlock(blockFamilyProperties(properties)));

    public static final DeferredBlock<Block> DIMENSIONAL_STONE_BRICKS = registerBlock("dimensional_stone_bricks",
            properties -> new Block(blockFamilyProperties(properties)));

    public static final DeferredBlock<Block> DIMENSIONAL_STONE_BRICK_STAIRS = registerBlock("dimensional_stone_brick_stairs",
            properties -> new StairBlock(DRBlocks.DIMENSIONAL_STONE_BRICKS.get().defaultBlockState(), blockFamilyProperties(properties)));

    public static final DeferredBlock<Block> DIMENSIONAL_STONE_BRICK_SLAB = registerBlock("dimensional_stone_brick_slab",
            properties -> new SlabBlock(blockFamilyProperties(properties)));

    public static final DeferredBlock<Block> DIMENSIONAL_STONE_BRICK_WALL = registerBlock("dimensional_stone_brick_wall",
            properties -> new WallBlock(blockFamilyProperties(properties)));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        DRItems.ITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    private static BlockBehaviour.Properties blockFamilyProperties(BlockBehaviour.Properties machineProperties) {
        return machineProperties
                .strength(2.5f, 2.5F)
                .requiresCorrectToolForDrops()
                .noOcclusion();
    }
}
