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

        // Calculate progression loop around the circle
        float angleZ = (sky.rotationZ() + (sky.speedZ() * gameTime)) % 360.0f;
        float radZ = (float) Math.toRadians(angleZ);

        // --- STYLE 1: NATURAL OVERHEAD RING ORBIT (When speed_x is EXACTLY 0) ---
        if (sky.speedX() == 0.0f) {

            // 1. Create the radius of your small orbit on a flat horizontal plane.
            // We use rotation_z as a custom size/radius modifier for this specific style!
            // If rotation_z is 0, we'll default to a nice natural radius size of 1.0
            float orbitRadius = sky.rotationZ() == 0.0f ? 1.0f : Math.abs(sky.rotationZ() / 10.0f);

            Vector3f pos = new Vector3f(
                    (float) Math.cos(radZ) * orbitRadius,
                    0.0f,
                    (float) Math.sin(radZ) * orbitRadius
            );

            // 2. LIFT the entire orbit plane straight up along the Y axis!
            // We use rotation_x to control how high above the player the ring floats.
            // A value like 2.0f or 3.0f pushes it comfortably high into the upper sky.
            float heightOffset = sky.rotationX() <= 0.0f ? 2.0f : sky.rotationX() / 20.0f;
            pos.y += heightOffset;

            // 3. Return the normalized direction vector.
            // Because the center is shifted UP, the direction vector naturally points
            // skyward for every single point along the loop. No clamping required!
            return new Vec3(pos.x, pos.y, pos.z).normalize();
        }

        // --- STYLE 2: FULL REVOLUTION / UNDERWORLD (When speed_x is NOT 0) ---
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