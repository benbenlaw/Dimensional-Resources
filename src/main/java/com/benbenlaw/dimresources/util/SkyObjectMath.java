package com.benbenlaw.dimresources.util;

import com.benbenlaw.dimresources.loader.SkyObjectData;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SkyObjectMath {

    public static Vec3 direction(SkyObjectData sky) {
        Minecraft mc = Minecraft.getInstance();
        float gameTime = 0.0f;

        if (mc.level != null) {
            DeltaTracker deltaTracker = mc.getDeltaTracker();
            gameTime = (float) mc.level.getGameTime() + deltaTracker.getGameTimeDeltaTicks();
        }

        float angleZ = (sky.rotationZ() + (sky.speedZ() * gameTime)) % 360.0f;
        float radZ = (float) Math.toRadians(angleZ);

        if (sky.speedX() == 0.0f) {

            float orbitRadius = sky.rotationZ() == 0.0f ? 1.0f : Math.abs(sky.rotationZ() / 10.0f);

            Vector3f pos = new Vector3f(
                    (float) Math.cos(radZ) * orbitRadius,
                    0.0f,
                    (float) Math.sin(radZ) * orbitRadius
            );

            float heightOffset = sky.rotationX() <= 0.0f ? 2.0f : sky.rotationX() / 20.0f;
            pos.y += heightOffset;
            return new Vec3(pos.x, pos.y, pos.z).normalize();
        }

        else {
            Vector3f pos = new Vector3f(
                    (float) Math.cos(radZ),
                    (float) Math.sin(radZ),
                    0.0f
            );

            float angleX = (sky.rotationX() + (sky.speedX() * gameTime)) % 360.0f;
            float radX = (float) Math.toRadians(angleX);

            Matrix4f tiltMatrix = new Matrix4f().rotationY(radX);
            pos.mulDirection(tiltMatrix);

            return new Vec3(pos.x, pos.y, pos.z).normalize();
        }
    }
}