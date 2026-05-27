package com.benbenlaw.dimresources.data;

import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.block.DRBlocks;
import com.benbenlaw.dimresources.item.DRItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;


public class DRRecipeProvider extends RecipeProvider {

    public DRRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new DRRecipeProvider(provider, recipeOutput);
        }

        @Override
        public @NotNull String getName() {
            return DimResources.MOD_ID + " Recipes";
        }
    }

    @Override
    protected void buildRecipes() {

        //Planet Locator
        shaped(RecipeCategory.MISC, DRItems.PLANET_LOCATOR)
                .pattern(" B ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', Items.COMPASS)
                .define('B', DRItems.DIMENSIONAL_SHARD)
                .unlockedBy("has_shard", has(DRItems.DIMENSIONAL_SHARD.get()))
                .save(output);

        //Stone Stuff
        shaped(RecipeCategory.MISC, DRBlocks.DIMENSIONAL_STONE)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Tags.Items.STONES)
                .define('B', DRItems.DIMENSIONAL_SHARD)
                .unlockedBy("has_shard", has(DRItems.DIMENSIONAL_SHARD.get()))
                .save(output);

        twoByTwoPacker(RecipeCategory.MISC, DRBlocks.DIMENSIONAL_STONE_BRICKS, DRBlocks.DIMENSIONAL_STONE);
        wall(RecipeCategory.MISC, DRBlocks.DIMENSIONAL_STONE_WALL, DRBlocks.DIMENSIONAL_STONE);
        wall(RecipeCategory.MISC, DRBlocks.DIMENSIONAL_STONE_BRICK_WALL, DRBlocks.DIMENSIONAL_STONE_BRICKS);
        stairBuilder(DRBlocks.DIMENSIONAL_STONE_STAIRS, Ingredient.of(DRBlocks.DIMENSIONAL_STONE.get())).unlockedBy("has_shard", has(DRItems.DIMENSIONAL_SHARD.get())).save(output);
        stairBuilder(DRBlocks.DIMENSIONAL_STONE_BRICK_STAIRS, Ingredient.of(DRBlocks.DIMENSIONAL_STONE_BRICKS.get())).unlockedBy("has_shard", has(DRItems.DIMENSIONAL_SHARD.get())).save(output);
        slabBuilder(RecipeCategory.MISC, DRBlocks.DIMENSIONAL_STONE_SLAB, Ingredient.of(DRBlocks.DIMENSIONAL_STONE.get())).unlockedBy("has_shard", has(DRItems.DIMENSIONAL_SHARD.get())).save(output);
        slabBuilder(RecipeCategory.MISC, DRBlocks.DIMENSIONAL_STONE_BRICK_SLAB, Ingredient.of(DRBlocks.DIMENSIONAL_STONE_BRICKS.get())).unlockedBy("has_shard", has(DRItems.DIMENSIONAL_SHARD.get())).save(output);


        //Tank
        //shaped(RecipeCategory.MISC, CastingBlocks.TANK)
        //        .pattern(" A ")
        //        .pattern("ABA")
        //        .pattern(" A ")
        //        .define('A', CastingItems.BLACK_BRICK)
        //        .define('B', CastingBlocks.BLACK_BRICK_GLASS)
        //        .unlockedBy("has_clay", has(CastingItems.BLACK_BRICK))
        //        .save(output, "casting:crafting/tank");

    }

    protected void twoByTwoPacker(RecipeCategory category, ItemLike result, ItemLike ingredient, String id) {
        this.shaped(category, result, 1).define('#', ingredient).pattern("##").pattern("##").unlockedBy(getHasName(ingredient), this.has(ingredient)).save(this.output, DimResources.identifier(id).toString());
    }


    protected void wall(RecipeCategory category, ItemLike result, ItemLike base,  String id) {
        this.wallBuilder(category, result, Ingredient.of(base)).unlockedBy(getHasName(base), this.has(base)).save(this.output, DimResources.identifier(id).toString());
    }
}
