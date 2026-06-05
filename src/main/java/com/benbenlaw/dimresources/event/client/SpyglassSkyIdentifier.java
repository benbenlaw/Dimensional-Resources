package com.benbenlaw.dimresources.event.client;

import com.benbenlaw.dimresources.item.DRDataComponent;
import com.benbenlaw.dimresources.item.DRItems;
import com.benbenlaw.dimresources.item.PlanetLocatorItem;
import com.benbenlaw.dimresources.loader.SkyObjectData;
import com.benbenlaw.dimresources.loader.SkyObjectLoader;
import com.benbenlaw.dimresources.network.packet.SyncPlanetLocatorStack;
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
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import javax.annotation.Nullable;
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

        if (!player.isScoping()) return;

        if (stack.has(DRDataComponent.PLANET)) return;

        Identifier foundId = getLookedAtPlanet(player);

        if (foundId != null) {
            player.sendSystemMessage(Component.translatable("chat.dimresources.discovered_planet", ClientEvents.formatPlanetName(foundId)));

            stack.set(DRDataComponent.PLANET, foundId);

            ClientPacketDistributor.sendToServer(
                    new SyncPlanetLocatorStack(stack)
            );
        }
    }

    public static @Nullable Identifier getLookedAtPlanet(Player player) {

        Vec3 look = player.getLookAngle().normalize();

        Identifier foundId = null;
        double bestAngle = 0.75;

        for (Map.Entry<Identifier, SkyObjectData> entry : SkyObjectLoader.SKY_OBJECTS.entrySet()) {

            SkyObjectData sky = entry.getValue();
            Vec3 dir = SkyObjectMath.direction(sky).normalize();

            double dot = look.dot(dir);

            dot = Math.clamp(dot, -1.0, 1.0);

            double angle = Math.toDegrees(Math.acos(dot));

            if (angle < bestAngle) {
                bestAngle = angle;
                foundId = entry.getKey();
            }
        }

        return foundId;
    }
}