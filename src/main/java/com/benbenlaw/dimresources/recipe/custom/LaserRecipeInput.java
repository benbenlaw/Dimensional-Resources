package com.benbenlaw.dimresources.recipe.custom;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.transfer.item.ItemUtil;

public class LaserRecipeInput implements RecipeInput {

    private final Identifier planet;
    private final int laserLevel;

    public LaserRecipeInput(Identifier planet, int laserLevel) {
        this.planet = planet;
        this.laserLevel = laserLevel;
    }

    public Identifier getPlanet() {
        return planet;
    }

    public int getLaserLevel() {
        return laserLevel;
    }

    @Override
    public ItemStack getItem(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}