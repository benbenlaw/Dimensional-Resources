package com.benbenlaw.dimresources.event;

import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.block.DRBlockEntities;
import com.benbenlaw.dimresources.loader.SkyObjectLoader;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;

@EventBusSubscriber(modid = DimResources.MOD_ID)
public class DREvents {

    @SubscribeEvent
    public static void onAddReloadListener(AddServerReloadListenersEvent event) {
        event.addListener(DimResources.identifier("modifiers"), new SkyObjectLoader());
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

        event.registerBlockEntity(Capabilities.Item.BLOCK, DRBlockEntities.LASER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getItemHandler());

        event.registerBlockEntity(Capabilities.Energy.BLOCK, DRBlockEntities.LASER_BLOCK_ENTITY.get(),
                (blockEntity, side) -> blockEntity.getEnergyHandler());


    }

}

