package com.benbenlaw.dimresources.data;

import com.benbenlaw.core.block.SyncableBlock;
import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.block.DRBlocks;
import com.benbenlaw.dimresources.item.DRItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static net.minecraft.client.data.models.BlockModelGenerators.*;

public class DRModelProvider extends ModelProvider {

    public DRModelProvider(PackOutput output) {
        super(output, DimResources.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        //Items
        itemModels.generateFlatItem(DRItems.DIMENSIONAL_SHARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(DRItems.PLANET_LOCATOR.get(), ModelTemplates.FLAT_ITEM);
        
        //Blocks
        createMachineBlock(DRBlocks.LASER.get(), blockModels.blockStateOutput, blockModels.modelOutput);
        blockModels.createTrivialCube(DRBlocks.DEEPSLATE_DIMENSIONAL_ORE.get());
        blockModels.createTrivialCube(DRBlocks.DIMENSIONAL_ORE.get());

        blockModels.family(DRBlocks.DIMENSIONAL_STONE.get())
                .stairs(DRBlocks.DIMENSIONAL_STONE_STAIRS.get())
                .wall(DRBlocks.DIMENSIONAL_STONE_WALL.get())
                .slab(DRBlocks.DIMENSIONAL_STONE_SLAB.get());

        blockModels.family(DRBlocks.DIMENSIONAL_STONE_BRICKS.get())
                .stairs(DRBlocks.DIMENSIONAL_STONE_BRICK_STAIRS.get())
                .wall(DRBlocks.DIMENSIONAL_STONE_BRICK_WALL.get())
                .slab(DRBlocks.DIMENSIONAL_STONE_BRICK_SLAB.get());


    }
    
    public void createMachineBlock(Block block, Consumer<BlockModelDefinitionGenerator> blockStateOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        TextureMapping idleTextureMapping = (new TextureMapping()).put(TextureSlot.TOP, new Material(DimResources.identifier("block/machine_top"))).put(TextureSlot.SIDE, new Material(DimResources.identifier("block/machine_side_idle"))).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front"));
        TextureMapping workingTextureMapping = (new TextureMapping()).put(TextureSlot.TOP, new Material(DimResources.identifier("block/machine_top"))).put(TextureSlot.SIDE, new Material(DimResources.identifier("block/machine_side_working"))).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front_working"));

        MultiVariant multivariant = plainVariant(ModelTemplates.CUBE_ORIENTABLE.create(block, idleTextureMapping, modelOutput));
        MultiVariant multivariant1 = plainVariant(ModelTemplates.CUBE_ORIENTABLE_VERTICAL.create(block, idleTextureMapping, modelOutput));

        MultiVariant workingVariant = plainVariant(ModelTemplates.CUBE_ORIENTABLE.createWithSuffix(block, "_working", workingTextureMapping, modelOutput));
        MultiVariant workingVariant1 = plainVariant(ModelTemplates.CUBE_ORIENTABLE_VERTICAL.createWithSuffix(block, "_working", workingTextureMapping, modelOutput));

        blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(PropertyDispatch.initial(BlockStateProperties.FACING, SyncableBlock.RUNNING)
                                .select(Direction.DOWN, false, multivariant1.with(X_ROT_180))
                                .select(Direction.UP, false, multivariant1)
                                .select(Direction.NORTH, false, multivariant)
                                .select(Direction.EAST, false, multivariant.with(Y_ROT_90))
                                .select(Direction.SOUTH,false, multivariant.with(Y_ROT_180))
                                .select(Direction.WEST,false, multivariant.with(Y_ROT_270))
                                .select(Direction.DOWN, true, workingVariant1.with(X_ROT_180))
                                .select(Direction.UP, true, workingVariant1)
                                .select(Direction.NORTH, true, workingVariant)
                                .select(Direction.EAST, true, workingVariant.with(Y_ROT_90))
                                .select(Direction.SOUTH,true, workingVariant.with(Y_ROT_180))
                                .select(Direction.WEST,true, workingVariant.with(Y_ROT_270))));

    }

}
