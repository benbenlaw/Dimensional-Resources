package com.benbenlaw.dimresources.event;

import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.block.DRBlockEntities;
import com.benbenlaw.dimresources.loader.SkyObjectLoader;
import com.benbenlaw.dimresources.recipe.DRRecipes;
import com.benbenlaw.dimresources.recipe.custom.LaserRecipe;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    @SubscribeEvent
    public static void onDataPackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(DRRecipes.LASER_TYPE.get());
    }

    @SubscribeEvent
    public static void onRecipeReceived(RecipesReceivedEvent event) {
        RecipeMap recipeMap = event.getRecipeMap();

        Collection<RecipeHolder<LaserRecipe>> meltingRecipe = recipeMap.byType(DRRecipes.LASER_TYPE.get());
        Map<Identifier, LaserRecipe> laserRecipeMap = new HashMap<>();

        for (RecipeHolder<LaserRecipe> recipeHolder : meltingRecipe) {
            laserRecipeMap.put(recipeHolder.id().identifier(), recipeHolder.value());
        }
        ClientRecipeCache.setCachedLaserRecipes(laserRecipeMap);
    }
}

