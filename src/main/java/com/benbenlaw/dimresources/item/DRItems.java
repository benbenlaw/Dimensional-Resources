package com.benbenlaw.dimresources.item;

import com.benbenlaw.dimresources.DimResources;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DRItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DimResources.MOD_ID);

    //Simple items
    public static final DeferredItem<Item> DIMENSIONAL_SHARD = ITEMS.registerSimpleItem("dimensional_shard");

    public static final DeferredItem<Item> PLANET_LOCATOR = ITEMS.registerItem("planet_locator",
            PlanetLocatorItem::new, properties -> properties
                    .stacksTo(1));
}
