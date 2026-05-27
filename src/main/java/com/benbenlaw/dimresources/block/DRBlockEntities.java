package com.benbenlaw.dimresources.block;

import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.block.entity.LaserBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DRBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, DimResources.MOD_ID);

    public static final Supplier<BlockEntityType<LaserBlockEntity>> LASER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("laser_block_entity", () ->
                    new BlockEntityType<>(LaserBlockEntity::new, DRBlocks.LASER.get()));

}
