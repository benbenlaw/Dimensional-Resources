package com.benbenlaw.dimresources.screen;

import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.screen.custom.LaserMenu;
import com.benbenlaw.dimresources.screen.custom.LaserScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DRMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS =  DeferredRegister.create(BuiltInRegistries.MENU, DimResources.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<LaserMenu>> LASER_MENU =
            MENUS.register("laser_menu", () -> IMenuTypeExtension.create(LaserMenu::new));
}
