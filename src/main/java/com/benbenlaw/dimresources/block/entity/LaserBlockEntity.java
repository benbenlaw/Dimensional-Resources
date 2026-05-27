package com.benbenlaw.dimresources.block.entity;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.item.SyncableItemHandler;
import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.block.DRBlockEntities;
import com.benbenlaw.dimresources.block.DRBlocks;
import com.benbenlaw.dimresources.item.DRDataComponent;
import com.benbenlaw.dimresources.recipe.custom.LaserRecipe;
import com.benbenlaw.dimresources.recipe.custom.LaserRecipeInput;
import com.benbenlaw.dimresources.screen.custom.LaserMenu;
import com.benbenlaw.dimresources.util.EnergyHandler;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LaserBlockEntity extends SyncableBlockEntity implements MenuProvider {

    private final ContainerData data;
    private int[] progress = new int[6];
    private int[] maxProgress = new int[] {20, 20, 20, 20, 20, 20};
    public int laserLevel;

    public List<Identifier> targetSkyObjects = new ArrayList<>();
    private final EnergyHandler energyHandler = new EnergyHandler(10000000, 1000000, this);

    private final SyncableItemHandler inventory = new SyncableItemHandler(this, 12,
            (slot, stack) -> slot < 6,
            slot -> slot >= 6
    );

    public LaserBlockEntity(BlockPos pos, BlockState state) {
        super(DRBlockEntities.LASER_BLOCK_ENTITY.get(), pos, state);
        this.data = new ContainerData() {

            @Override
            public int get(int index) {
                return switch (index) {
                    case 0, 1, 2, 3, 4, 5 -> progress[index];
                    case 6, 7, 8, 9, 10, 11 -> maxProgress[index - 6];
                    case 12 -> laserLevel;
                    case 13 -> energyHandler.getAmountAsInt();
                    case 14 -> energyHandler.getCapacityAsInt();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0, 1, 2, 3, 4, 5 -> progress[index] = value;
                    case 6, 7, 8, 9, 10, 11 -> maxProgress[index - 6] = value;
                    case 12 -> laserLevel = value;
                    case 13 -> energyHandler.getAmountAsInt();
                    case 14 -> energyHandler.getCapacityAsInt();
                }
            }

            @Override
            public int getCount() {
                return 15;
            }
        };
    }

    public void tick() {
        assert level != null;
        if (level.isClientSide()) return;

        if (level.getGameTime() % 80 == 0) {
            laserLevel = updateBase(level,worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
            targetSkyObjects = getSeenSkyObjects();
            sync();
        }

        for (int i = 0; i < 6; i++) {

            ItemStack input = ItemUtil.getStack(inventory, i);

            if (input.isEmpty()) {
                progress[i] = 0;
                maxProgress[i] = 20;
                continue;
            }

            LaserRecipe recipe = findRecipeForSlot(i);

            if (recipe == null) {
                progress[i] = 0;
                maxProgress[i] = 20;
                continue;
            }

            maxProgress[i] = recipe.duration();

            ItemStack output = recipe.rollOutput(level.getRandom());

            if (!canInsert(i, output)) {
                continue;
            }

            int energyPerTick = recipe.rfPerTick();

            if (energyHandler.getAmountAsInt() < energyPerTick) {
                continue;
            }

            try (Transaction tx = Transaction.openRoot()) {
                energyHandler.extract(energyPerTick, tx);
                tx.commit();
            }

            progress[i]++;

            if (progress[i] >= recipe.duration()) {
                insertOutput(getOutputSlot(i), output);
                progress[i] = 0;
            }
        }

        sync();
    }

    public List<Identifier> getSeenSkyObjects() {

        List<Identifier> result = new ArrayList<>();

        for (int slot = 0; slot < 6; slot++) {
            ItemStack input = ItemUtil.getStack(inventory, slot);

            if (!input.isEmpty() && input.has(DRDataComponent.PLANET)) {
                Identifier planet = input.get(DRDataComponent.PLANET);
                result.add(planet);
            }
        }

        return result;
    }

    @Nullable
    private LaserRecipe findRecipeForSlot(int slot) {


        ItemStack input = ItemUtil.getStack(inventory, slot);
        Identifier planet;

        if (input.has(DRDataComponent.PLANET)) {
            planet = input.get(DRDataComponent.PLANET);
        } else {
            planet = null;
        }

        if (planet == null) return null;

        LaserRecipeInput recipeInput = new LaserRecipeInput(planet, laserLevel);

        assert level != null;

        return Objects.requireNonNull(level.getServer())
                .getRecipeManager()
                .recipeMap()
                .getRecipesFor(LaserRecipe.TYPE, recipeInput, level)
                .filter(holder -> holder.value().matches(recipeInput, level))
                .map(RecipeHolder::value)
                .findFirst()
                .orElse(null);
    }

    private boolean canInsert(int inputSlot, ItemStack stack) {
        int outputSlot = getOutputSlot(inputSlot);
        ItemStack existing = ItemUtil.getStack(inventory, outputSlot);

        return existing.isEmpty()
                || (ItemStack.isSameItemSameComponents(existing, stack)
                && existing.getCount() + stack.getCount() <= existing.getMaxStackSize());
    }

    private void insertOutput(int slot, ItemStack stack) {
        ItemStack existing = ItemUtil.getStack(inventory, slot);

        if (existing.isEmpty()) {
            inventory.set(slot, ItemResource.of(stack), stack.getCount());
        } else if (ItemStack.isSameItemSameComponents(existing, stack)) {
            existing.grow(stack.getCount());
            inventory.set(slot, ItemResource.of(stack), existing.count());
        }
    }

    private static int updateBase(Level level, int x, int y, int z) {
        int levels = 0;

        for (int step = 1; step <= 12; step++) {
            int ly = y - step;

            if (ly < level.getMinY()) {
                break;
            }

            boolean isOk = true;

            // 3x3 area
            for (int lx = x - 1; lx <= x + 1 && isOk; lx++) {
                for (int lz = z - 1; lz <= z + 1; lz++) {

                    BlockPos pos = new BlockPos(lx, ly, lz);

                    if (!level.getBlockState(pos)
                            .is(DRBlocks.DIMENSIONAL_STONE)) {

                        isOk = false;
                        break;
                    }
                }
            }

            // Corner check
            if (isOk) {
                BlockPos[] corners = new BlockPos[] {
                        new BlockPos(x - 2, ly, z - 2),
                        new BlockPos(x + 2, ly, z - 2),
                        new BlockPos(x - 2, ly, z + 2),
                        new BlockPos(x + 2, ly, z + 2)
                };

                for (BlockPos corner : corners) {
                    if (!level.getBlockState(corner).is(DRBlocks.DIMENSIONAL_STONE_BRICKS)) {
                        isOk = false;
                        break;
                    }
                }
            }

            if (!isOk) {
                break;
            }

            levels++;
        }

        return levels;
    }

    private int getOutputSlot(int inputSlot) {
        return inputSlot + 6;
    }

    public ItemStacksResourceHandler getItemHandler() {
        return inventory;
    }

    public EnergyHandler getEnergyHandler() {
        return energyHandler;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int container, Inventory inventory, Player player) {
        return new LaserMenu(container, inventory, this.worldPosition, data);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.dimresources.laser");
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {

        inventory.serialize(output.child("inventory"));
        energyHandler.serialize(output.child("energy"));
        output.putIntArray("maxProgress", maxProgress);
        output.putIntArray("progress", progress);
        output.putInt("laserLevel", laserLevel);

        if (!targetSkyObjects.isEmpty()) {
            output.store("targetSkyObjects", Identifier.CODEC.listOf(), targetSkyObjects);
        }

        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {

        inventory.deserialize(input.childOrEmpty("inventory"));
        energyHandler.deserialize(input.childOrEmpty("energy"));
        maxProgress = input.getIntArray("maxProgress").orElse(new int[] {20, 20, 20, 20, 20, 20});
        progress = input.getIntArray("progress").orElse(new int[] {0, 0, 0, 0, 0, 0});
        laserLevel = input.getIntOr("laserLevel", 0);

        targetSkyObjects = new ArrayList<>(
                input.read("targetSkyObjects", Identifier.CODEC.listOf())
                        .orElse(List.of())
        );
        super.loadAdditional(input);
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        dropInventoryContents(inventory);
    }
}
