package com.benbenlaw.dimresources.event.client;

import com.benbenlaw.dimresources.loader.SkyObjectData;
import com.benbenlaw.dimresources.loader.SkyObjectLoader;
import com.benbenlaw.dimresources.util.SkyObjectMath;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.*;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;

@EventBusSubscriber(value = Dist.CLIENT)
public class SkyRenderHandler {

    private static final Map<Identifier, GpuBuffer> CACHED_BUFFERS = new HashMap<>();

    @SubscribeEvent
    public static void renderSky(RenderLevelStageEvent.AfterSky event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null || SkyObjectLoader.SKY_OBJECTS.isEmpty()) return;

        Identifier dimension = mc.level.dimension().identifier();
        PoseStack poseStack = event.getPoseStack();

        float rainBrightness = mc.level.getRainLevel(1.0F);
        AtlasManager atlasManager = mc.getAtlasManager();
        TextureAtlas celestialsAtlas = atlasManager.getAtlasOrThrow(AtlasIds.CELESTIALS);

        RenderSystem.AutoStorageIndexBuffer quadIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        GpuBuffer indexBuffer = quadIndices.getBuffer(6);

        GpuTextureView color = mc.getMainRenderTarget().getColorTextureView();
        GpuTextureView depth = mc.getMainRenderTarget().getDepthTextureView();

        for (Map.Entry<Identifier, SkyObjectData> entry : SkyObjectLoader.SKY_OBJECTS.entrySet()) {
            Identifier id = entry.getKey();
            SkyObjectData sky = entry.getValue();

            if (!sky.isValidIn(dimension)) continue;

            Vec3 skyObjectDirection = SkyObjectMath.direction(sky);
            float distance = sky.distance();
            float size = sky.size();

            GpuBuffer cachedBuffer = CACHED_BUFFERS.computeIfAbsent(sky.texture(), textureId -> {
                TextureAtlasSprite sprite = celestialsAtlas.getSprite(textureId);
                return buildCelestialQuad("Custom Sky Object: " + id.toString(), sprite);
            });

            Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushMatrix();
            modelViewStack.mul(poseStack.last().pose());

            Vector3f dir = new Vector3f((float) skyObjectDirection.x, (float) skyObjectDirection.y, (float) skyObjectDirection.z);
            Quaternionf skyRotation = new Quaternionf().rotationTo(new Vector3f(0, 1, 0), dir);

            Matrix4f rotationMatrix = new Matrix4f().rotation(skyRotation);
            modelViewStack.mul(rotationMatrix);

            modelViewStack.translate(0.0F, distance, 0.0F);
            modelViewStack.scale(size, 1.0F, size);

            GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
                    .writeTransform(modelViewStack,
                            new Vector4f(1.0F, 1.0F, 1.0F, 1.0F - rainBrightness),
                            new Vector3f(),
                            new Matrix4f());

            try (RenderPass renderPass = RenderSystem.getDevice()
                    .createCommandEncoder()
                    .createRenderPass(() -> "Custom Sky Sun Loop", color, OptionalInt.empty(), depth, OptionalDouble.empty())) {

                renderPass.setPipeline(RenderPipelines.CELESTIAL);
                RenderSystem.bindDefaultUniforms(renderPass);

                renderPass.setUniform("DynamicTransforms", dynamicTransforms);
                renderPass.bindTexture("Sampler0", celestialsAtlas.getTextureView(), celestialsAtlas.getSampler());

                renderPass.setVertexBuffer(0, cachedBuffer);
                renderPass.setIndexBuffer(indexBuffer, quadIndices.type());

                renderPass.drawIndexed(0, 0, 6, 1);
            }

            modelViewStack.popMatrix();
        }
    }

    private static GpuBuffer buildCelestialQuad(String name, TextureAtlasSprite sprite) {
        VertexFormat format = DefaultVertexFormat.POSITION_TEX;
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(4 * format.getVertexSize())) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, format);

            bufferBuilder.addVertex(-1.0F, 0.0F, -1.0F).setUv(sprite.getU0(), sprite.getV0());
            bufferBuilder.addVertex( 1.0F, 0.0F, -1.0F).setUv(sprite.getU1(), sprite.getV0());
            bufferBuilder.addVertex( 1.0F, 0.0F,  1.0F).setUv(sprite.getU1(), sprite.getV1());
            bufferBuilder.addVertex(-1.0F, 0.0F,  1.0F).setUv(sprite.getU0(), sprite.getV1());

            MeshData mesh = bufferBuilder.buildOrThrow();
            GpuBuffer buf = RenderSystem.getDevice().createBuffer(() -> name, 32, mesh.vertexBuffer());
            mesh.close();
            return buf;
        }
    }
}