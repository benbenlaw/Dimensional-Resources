package com.benbenlaw.dimresources.recipe;

import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.recipe.custom.LaserRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DRRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, DimResources.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, DimResources.MOD_ID);

    //Laser
    public static final Supplier<RecipeSerializer<LaserRecipe>> LASER_SERIALIZER =
            SERIALIZER.register("laser", () -> LaserRecipe.SERIALIZER);
    public static final Supplier<RecipeType<LaserRecipe>> LASER_TYPE =
            TYPES.register("laser", () -> LaserRecipe.TYPE);


}
