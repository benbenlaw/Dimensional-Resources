package com.benbenlaw.dimresources.item;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PlanetLocatorItem extends Item {
    public PlanetLocatorItem(Properties properties) {
        super(properties);
    }

    public static ItemStack createPlanetLocatorItem(Identifier planet) {
        ItemStack stack = new ItemStack(DRItems.PLANET_LOCATOR.get());
        stack.set(DRDataComponent.PLANET, planet);
        return stack;
    }

}
