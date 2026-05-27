package com.benbenlaw.dimresources.event.client;

import com.benbenlaw.dimresources.item.DRDataComponent;
import com.benbenlaw.dimresources.item.DRItems;
import com.benbenlaw.dimresources.item.PlanetLocatorItem;
import com.benbenlaw.dimresources.loader.SkyObjectData;
import com.benbenlaw.dimresources.loader.SkyObjectLoader;
import com.benbenlaw.dimresources.util.SkyObjectMath;
import com.sun.jna.platform.unix.solaris.LibKstat;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.Map;

@EventBusSubscriber(value = Dist.CLIENT)
public class SpyglassSkyIdentifier {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;
        ItemStack stack = player.getOffhandItem();

        if (!stack.is(DRItems.PLANET_LOCATOR)) return;
        if (stack.has(DRDataComponent.PLANET)) return;

        if (!player.isScoping())
            return;

        Vec3 look = player.getLookAngle().normalize();

        Identifier foundId = null;
        double bestDot = 0.0;

        for (Map.Entry<Identifier, SkyObjectData> entry : SkyObjectLoader.SKY_OBJECTS.entrySet()) {

            Identifier id = entry.getKey();
            SkyObjectData sky = entry.getValue();

            Vec3 dir = SkyObjectMath.direction(sky).normalize();

            double dot = look.dot(dir);
            double threshold = 0.995;

            if (dot > threshold && dot > bestDot) {
                bestDot = dot;
                foundId = id;
            }
        }

        if (foundId != null) {
            player.sendSystemMessage(Component.translatable("chat.dimresources.discovered_planet", ClientEvents.formatPlanetName(foundId)));
            stack.set(DRDataComponent.PLANET, foundId);
        }
    }
}