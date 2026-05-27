package com.benbenlaw.dimresources.data;

import com.benbenlaw.dimresources.block.DRBlocks;
import com.benbenlaw.dimresources.item.DRItems;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class DRLootTableProvider extends VanillaBlockLoot {

    private final Set<Block> knownBlocks = new ReferenceOpenHashSet<>();

    public DRLootTableProvider(HolderLookup.Provider provider) {
        super(provider);
    }

    @Override
    protected void generate() {

        this.createDimensionalOreDrops(DRBlocks.DIMENSIONAL_ORE.get());
        this.createDimensionalOreDrops(DRBlocks.DEEPSLATE_DIMENSIONAL_ORE.get());
        this.dropSelf(DRBlocks.LASER.get());

        this.dropSelf(DRBlocks.DIMENSIONAL_STONE.get());
        this.dropSelf(DRBlocks.DIMENSIONAL_STONE_WALL.get());
        this.add(DRBlocks.DIMENSIONAL_STONE_SLAB.get(), this::createSlabItemTable);
        this.dropSelf(DRBlocks.DIMENSIONAL_STONE_BRICK_WALL.get());

        this.dropSelf(DRBlocks.DIMENSIONAL_STONE_BRICKS.get());
        this.dropSelf(DRBlocks.DIMENSIONAL_STONE_BRICK_WALL.get());
        this.add(DRBlocks.DIMENSIONAL_STONE_BRICK_SLAB.get(), this::createSlabItemTable);
        this.dropSelf(DRBlocks.DIMENSIONAL_STONE_BRICK_WALL.get());

    }

    protected void createDimensionalOreDrops(Block block) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        LootTable.Builder table = this.createSilkTouchDispatchTable(
                block,
                this.applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(DRItems.DIMENSIONAL_SHARD.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                .apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE))))
        );

        this.add(block, table);
    }

    @Override
    protected void add(@NotNull Block block, @NotNull LootTable.Builder table) {
        super.add(block, table);
        knownBlocks.add(block);
    }

    @NotNull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return knownBlocks;
    }
}