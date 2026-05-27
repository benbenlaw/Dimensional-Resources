package com.benbenlaw.dimresources.block.entity.renderer;

import com.benbenlaw.dimresources.block.entity.LaserBlockEntity;
import com.benbenlaw.dimresources.loader.SkyObjectData;
import com.benbenlaw.dimresources.loader.SkyObjectLoader;
import com.benbenlaw.dimresources.util.SkyObjectMath;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class LaserBlockEntityRenderer implements BlockEntityRenderer<LaserBlockEntity, LaserBlockEntityRenderState> {

    private static final RenderType LASER_BEAM = RenderType.create("laser_beam", RenderSetup.builder(RenderPipelines.BEACON_BEAM_OPAQUE)
            .withTexture("Sampler0", Identifier.withDefaultNamespace("textures/entity/beacon/beacon_beam.png"))
            .sortOnUpload().createRenderSetup()
    );

    public LaserBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public LaserBlockEntityRenderState createRenderState() {
        return new LaserBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(LaserBlockEntity blockEntity, LaserBlockEntityRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        state.blockPos = blockEntity.getBlockPos();
        state.cameraPosition = cameraPosition;
        state.laserLevel = blockEntity.laserLevel;
        state.targetSkyObjects = blockEntity.targetSkyObjects;

    }

    @Override
    public void submit(LaserBlockEntityRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {

        if (renderState.blockPos == null) return;
        if (renderState.laserLevel == 0) return;
        if (renderState.targetSkyObjects == null || renderState.targetSkyObjects.isEmpty()) return;

        Vec3 camera = cameraRenderState.pos;

        Vec3 beamStartWorld = new Vec3(renderState.blockPos.getX() + 0.5, renderState.blockPos.getY() + 0.5, renderState.blockPos.getZ() + 0.5);

        float time = (System.currentTimeMillis() % 10000L) / 10000.0f;
        float vOffset = -(time * 2f);

        for (Identifier targetId : renderState.targetSkyObjects) {

            if (targetId == null) continue;

            SkyObjectData sky = SkyObjectLoader.SKY_OBJECTS.get(targetId);
            if (sky == null) continue;

            int c = sky.beamColor();

            float r = ((c >> 16) & 255) / 255f;
            float g = ((c >> 8) & 255) / 255f;
            float b = (c & 255) / 255f;
            float a = ((c >> 24) & 255) / 255f;
            float beamRadius = sky.beamRadius();
            Vec3 skyDir = SkyObjectMath.direction(sky);
            Vec3 beamEndWorld = beamStartWorld.add(skyDir.scale(sky.distance() * 10));
            Vec3 beamStartRelCamera = beamStartWorld.subtract(camera);
            Vec3 end = beamEndWorld.subtract(camera);
            Vec3 delta = end.subtract(beamStartRelCamera);
            double length = delta.length();
            Vec3 dir = delta.normalize();

            submitNodeCollector.submitCustomGeometry(poseStack, LASER_BEAM, (pose, consumer) -> {

                PoseStack local = new PoseStack();

                local.translate((float) beamStartRelCamera.x, (float) beamStartRelCamera.y, (float) beamStartRelCamera.z);
                Quaternionf rotation = new Quaternionf().rotationTo(new Vector3f(0, 1, 0),
                        new Vector3f((float) dir.x, (float) dir.y, (float) dir.z)
                );

                local.mulPose(rotation);

                for (int face = 0; face < 4; face++) {
                    local.pushPose();
                    local.mulPose(Axis.YP.rotationDegrees(face * 90f));

                    PoseStack.Pose facePose = local.last();

                    addBeamVertex(consumer, facePose, -beamRadius, (float) length, -beamRadius, r, g, b, a,0f, vOffset + (float) length);
                    addBeamVertex(consumer, facePose, beamRadius, (float) length, -beamRadius, r, g, b, a,1f, vOffset + (float) length);
                    addBeamVertex(consumer, facePose, beamRadius, 0f, -beamRadius, r, g, b, a, 1f, vOffset);
                    addBeamVertex(consumer, facePose, -beamRadius, 0f, -beamRadius, r, g, b, a,0f, vOffset);
                    local.popPose();
                }
            });
        }
    }
    private static void addBeamVertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float r, float g, float b, float a, float u, float v) {
        consumer.addVertex(pose, x, y, z)
                .setColor(r, g, b, a)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(0xF000F0)
                .setNormal(pose, 0f, 1f, 0f);
    }

    @Override public boolean shouldRender(LaserBlockEntity be, Vec3 cameraPos) {
        return true;
    }

    @Override public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override public int getViewDistance() {
        return 64;
    }

    @Override
    public @NonNull AABB getRenderBoundingBox(LaserBlockEntity blockEntity) {
        return AABB.encapsulatingFullBlocks(
                blockEntity.getBlockPos().above(32).north(32).east(32),
                blockEntity.getBlockPos().below(32).south(32).west(32)
        );
    }
}