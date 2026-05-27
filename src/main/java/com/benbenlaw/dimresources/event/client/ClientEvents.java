package com.benbenlaw.dimresources.event.client;

import com.benbenlaw.core.util.TooltipUtil;
import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.block.DRBlockEntities;
import com.benbenlaw.dimresources.block.DRBlocks;
import com.benbenlaw.dimresources.block.entity.renderer.LaserBlockEntityRenderer;
import com.benbenlaw.dimresources.item.DRDataComponent;
import com.benbenlaw.dimresources.screen.DRMenuTypes;
import com.benbenlaw.dimresources.screen.custom.LaserScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = DimResources.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(DRBlockEntities.LASER_BLOCK_ENTITY.get(), LaserBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(DRMenuTypes.LASER_MENU.get(), LaserScreen::new);
    }

    @SubscribeEvent
    public static void onTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (stack.has(DRDataComponent.PLANET.get())) {
            Identifier planet = stack.get(DRDataComponent.PLANET.get());

            String planetName = formatPlanetName(planet);

            if (Minecraft.getInstance().hasShiftDown()) {
                event.getToolTip().add(
                        Component.translatable("tooltip.dimresources.planet", planetName)
                                .withStyle(ChatFormatting.BLUE)
                );

            } else {
                event.getToolTip().add(
                        Component.translatable("tooltip.bblcore.shift")
                                .withStyle(ChatFormatting.YELLOW)
                );
            }
        }
    }

    public static String formatPlanetName(Identifier id) {
        String path = id.getPath();
        return Arrays.stream(path.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
