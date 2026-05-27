package com.benbenlaw.dimresources.integration.jei;

import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.event.ClientRecipeCache;
import com.benbenlaw.dimresources.item.DRItems;
import com.benbenlaw.dimresources.screen.custom.LaserScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

@JeiPlugin
public class DRJEIPlugin implements IModPlugin {

    public static IDrawableStatic slotDrawable;

    @Override
    public @NonNull Identifier getPluginUid() {
        return DimResources.identifier("jei_plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(DRItems.PLANET_LOCATOR.asItem(), new ItemSubtypeInterpreter());

    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        slotDrawable = registration.getJeiHelpers().getGuiHelper().getSlotDrawable();

        registration.addRecipeCategories(new LaserRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        registration.addRecipes(LaserRecipeCategory.RECIPE_TYPE, ClientRecipeCache.getCachedLaserRecipes().stream().toList());

    }

    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(LaserScreen.class, 52, 16, 24, 54, LaserRecipeCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(LaserScreen.class, 124, 16, 24, 54, LaserRecipeCategory.RECIPE_TYPE);
    }
}
