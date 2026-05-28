package com.benbenlaw.dimresources;

import com.benbenlaw.dimresources.block.DRBlockEntities;
import com.benbenlaw.dimresources.block.DRBlocks;
import com.benbenlaw.dimresources.item.DRCreativeTab;
import com.benbenlaw.dimresources.item.DRDataComponent;
import com.benbenlaw.dimresources.item.DRItems;
import com.benbenlaw.dimresources.recipe.DRRecipes;
import com.benbenlaw.dimresources.screen.DRMenuTypes;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DimResources.MOD_ID)
public class DimResources {

    public static final String MOD_ID = "dimresources";
    private static final Logger LOGGER = LogManager.getLogger();

    public DimResources(IEventBus modEventBus) {

        DRBlocks.BLOCKS.register(modEventBus);
        DRItems.ITEMS.register(modEventBus);
        DRBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        DRCreativeTab.CREATIVE_MODE_TABS.register(modEventBus);
        DRMenuTypes.MENUS.register(modEventBus);
        DRRecipes.SERIALIZER.register(modEventBus);
        DRRecipes.TYPES.register(modEventBus);
        DRDataComponent.COMPONENTS.register(modEventBus);
    }

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}

