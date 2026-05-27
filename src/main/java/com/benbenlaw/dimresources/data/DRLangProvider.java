package com.benbenlaw.dimresources.data;

import com.benbenlaw.dimresources.DimResources;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class DRLangProvider extends LanguageProvider {

    public DRLangProvider(PackOutput output) {
        super(output, DimResources.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {

        //Creative Tab
        add("itemGroup.dimresources", "Dimensional Resources");

        //Block
        addBlockTranslation("dimensional_ore", "Dimensional Ore");
        addBlockTranslation("deepslate_dimensional_ore", "Deepslate Dimensional Ore");
        addBlockTranslation("laser", "Interdimensional Laser");
        addBlockTranslation("dimensional_stone", "Dimensional Stone");
        addBlockTranslation("dimensional_stone_stairs", "Dimensional Stone Stairs");
        addBlockTranslation("dimensional_stone_slab", "Dimensional Stone Slab");
        addBlockTranslation("dimensional_stone_wall", "Dimensional Stone Wall");
        addBlockTranslation("dimensional_stone_bricks", "Dimensional Stone Bricks");
        addBlockTranslation("dimensional_stone_brick_stairs", "Dimensional Stone Brick Stairs");
        addBlockTranslation("dimensional_stone_brick_slab", "Dimensional Stone Brick Slab");
        addBlockTranslation("dimensional_stone_brick_wall", "Dimensional Stone Brick Wall");


        //Item
        addItemTranslation("dimensional_shard", "Dimensional Shard");
        addItemTranslation("planet_locator", "Planet Locator");

        //Tooltip
        add("tooltip.dimresources.planet", "Planet: %s");


    }

    private void addItemTranslation(String name, String translation) {
        add("item." + DimResources.MOD_ID + "." + name, translation);
    }
    private void addBlockTranslation(String name, String translation) {
        add("block." + DimResources.MOD_ID + "." + name, translation);
    }

}
