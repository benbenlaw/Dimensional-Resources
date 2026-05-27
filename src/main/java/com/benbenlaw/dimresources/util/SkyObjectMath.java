package com.benbenlaw.dimresources.util;

import com.benbenlaw.dimresources.loader.SkyObjectData;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

public class SkyObjectMath {

    public static Vec3 direction(SkyObjectData sky) {
        Minecraft mc = Minecraft.getInstance();
        float gameTime = 0.0f;

        if (mc.level != null) {
            // Get the modern delta tracker for frame-rate independent smooth movement
            DeltaTracker deltaTracker = mc.getDeltaTracker();

            // GameTime (long) + GameTimeDeltaTicks (float) = Smoothly moving time
            gameTime = (float) mc.level.getGameTime() + deltaTracker.getGameTimeDeltaTicks();
        }

        // Calculate current angles: Base Angle + (Speed * Time)
        float currentX = (sky.rotationX() + (sky.speedX() * gameTime)) % 360.0f;
        float currentZ = (sky.rotationZ() + (sky.speedZ() * gameTime)) % 360.0f;

        // Convert degrees from JSON into radians
        float rotX = (float) Math.toRadians(currentX);
        float rotZ = (float) Math.toRadians(currentZ);

        // Calculate the moving 3D unit vector
        double x = Math.sin(rotZ) * Math.cos(rotX);
        double y = Math.cos(rotZ) * Math.cos(rotX);
        double z = Math.sin(rotX);

        return new Vec3(x, y, z).normalize();
    }
}