package com.benbenlaw.dimresources.item;

import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.loader.SkyObjectLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DRCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DimResources.MOD_ID);

    public static final Supplier<CreativeModeTab> DIMENSIONAL_RESOURCES_TAB =
            CREATIVE_MODE_TABS.register(DimResources.MOD_ID, () ->
                    CreativeModeTab.builder()
                            .withTabsBefore(CreativeModeTabs.COMBAT)
                            .icon(() -> DRItems.DIMENSIONAL_SHARD.get()
                                    .asItem()
                                    .getDefaultInstance())
                            .title(Component.translatable("itemGroup." + DimResources.MOD_ID))

                            .displayItems((parameters, output) -> {

                                DRItems.ITEMS.getEntries().forEach(item ->
                                        output.accept(item.get())
                                );

                                SkyObjectLoader.SKY_OBJECTS.forEach((id, value) ->
                                        output.accept(
                                                PlanetLocatorItem.createPlanetLocatorItem(id)
                                        )
                                );
                            })

                            .build());
}