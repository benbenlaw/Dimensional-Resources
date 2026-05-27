package com.benbenlaw.dimresources.integration.jei;

import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.item.DRItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.Identifier;

@JeiPlugin
public class DRJEIPlugin implements IModPlugin {


    @Override
    public Identifier getPluginUid() {
        return DimResources.identifier("jei_plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(DRItems.PLANET_LOCATOR.asItem(), new ItemSubtypeInterpreter());

    }
}
