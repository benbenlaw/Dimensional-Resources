package com.benbenlaw.dimresources.block.entity.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class LaserBlockEntityRenderState extends BlockEntityRenderState {
    public BlockPos blockPos;
    public Vec3 cameraPosition;
    public int laserLevel;
    public List<Identifier> targetSkyObjects = new ArrayList<>();
}