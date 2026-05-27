package com.benbenlaw.dimresources.event;

import com.benbenlaw.dimresources.recipe.custom.LaserRecipe;
import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClientRecipeCache {

    public static Map<Identifier, LaserRecipe> cachedLaserRecipes = new HashMap<>();

    public static void setCachedLaserRecipes(Map<Identifier, LaserRecipe> cachedLaserRecipes) {
        ClientRecipeCache.cachedLaserRecipes = cachedLaserRecipes;
    }

    public static Collection<LaserRecipe> getCachedLaserRecipes() {
        return cachedLaserRecipes.values();
    }

}
